package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.convertor.GoalConvertor;
import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.UpdateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.facade.GoalRepositoryFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalServiceImpl implements GoalService {

    private final GoalRepositoryFacade goalRepositoryFacade;
    private final GoalConvertor goalConvertor;

    @Override
    @Transactional
    public CreateGoalResponseDTO createGoal(CreateGoalRequestDTO requestDTO, Long userId) {
        try {
            validateGoalCreation(requestDTO);
            validateBusinessRules(requestDTO, userId);

            Goal goal = goalConvertor.toEntity(requestDTO);
            Goal savedGoal = goalRepositoryFacade.saveGoal(goal);

            return goalConvertor.toCreateResponseDTO(savedGoal);
        } catch (GoalException e) {
            throw e;
        } catch (Exception e) {
            log.error("대목표 생성 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new GoalException(GoalExceptionType.GOAL_CREATION_FAILED);
        }
    }

    @Override
    public List<GoalResponseDTO> getGoalByUser(Long userId, LocalDateTime dateTime) {
        try{
            List<Goal> goals;
            if (dateTime != null) {
                goals = goalRepositoryFacade.findGoalByDate(dateTime);
            } else {
                goals = goalRepositoryFacade.findAllGoals();
            }
            return goalConvertor.toGoalResponseDTO(goals);
        }catch (GoalException e) {
            throw e;
        }catch (Exception e) {
            log.error("대목표 조회 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new GoalException(GoalExceptionType.GOAL_FOUND_FAILED);
        }
    }

    @Override
    public GoalResponseDTO getGoalByGoalId(Long userId, Long goalId) {
        try{
            if(goalId == null) {
                throw new GoalException(GoalExceptionType.GOAL_FOUND_EMPTY);
            }

            Goal goal = goalRepositoryFacade.findGoalByGoalId(goalId);

            if(goal == null) {
                throw new GoalException(GoalExceptionType.GOAL_FOUND_EMPTY);
            }

            return goalConvertor.toGoalResponseDTO(goal);
        }catch (GoalException e) {
            throw e;
        }catch (Exception e) {
            log.error("대목표 조회 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new GoalException(GoalExceptionType.GOAL_FOUND_FAILED);
        }
    }

    @Override
    @Transactional
    public GoalResponseDTO updateGoalById(Long goalId, UpdateGoalRequestDTO request, Long userId) {
        try {
            if (request.getTitle() != null) {
                validateTitle(request.getTitle());
            }
            if (request.getDuration() != null) {
                validateDuration(request.getDuration());
            }
            if (request.getStartDate() != null && request.getEndDate() != null) {
                validateDateRange(request.getStartDate(), request.getEndDate());
            }

            Goal existingGoal = goalRepositoryFacade.findById(goalId)
                    .orElseThrow(() -> new GoalException(GoalExceptionType.GOAL_NOT_FOUND));

            if (request.getTitle() != null) {
                existingGoal.setTitle(request.getTitle());
            }
            if (request.getPriority() != null) {
                existingGoal.setPriority(request.getPriority());
            }
            if (request.getStartDate() != null) {
                existingGoal.setStartDate(request.getStartDate());
            }
            if (request.getEndDate() != null) {
                existingGoal.setEndDate(request.getEndDate());
            }
            if (request.getDuration() != null) {
                existingGoal.setDuration(request.getDuration());
            }

            Goal goal = goalRepositoryFacade.updateGoal(existingGoal);

            return goalConvertor.toUpdateResponseDTO(goal);
        } catch (GoalException e) {
            throw e;
        } catch (Exception e) {
            log.error("대목표 생성 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new GoalException(GoalExceptionType.GOAL_CREATION_FAILED);
        }
    }


    private void validateGoalCreation(CreateGoalRequestDTO requestDTO) {
        validateTitle(requestDTO.getTitle());
        validateDuration(requestDTO.getDuration());
        validateDateRange(requestDTO.getStartDate(), requestDTO.getEndDate());
    }

    private void validateTitle(String title) {
        if (!StringUtils.hasText(title)) {
            throw new GoalException(GoalExceptionType.GOAL_TITLE_EMPTY);
        }
        if (title.length() > 200) {
            throw new GoalException(GoalExceptionType.GOAL_TITLE_TOO_LONG);
        }
    }

    private void validateDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new GoalException(GoalExceptionType.GOAL_DURATION_INVALID);
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new GoalException(GoalExceptionType.INVALID_GOAL_DATE_RANGE);
            }
        }
    }

    /**
     * 대목표 생성 시 비즈니스 규칙 검증을 수행합니다.
     * 
     * @param requestDTO 생성 요청 DTO
     * @param userId 사용자 ID
     */
    private void validateBusinessRules(CreateGoalRequestDTO requestDTO, Long userId) {
        // 시작 날짜가 과거인지 검증
        if (requestDTO.getStartDate() != null) {
            java.time.LocalDate today = java.time.LocalDate.now();
            if (requestDTO.getStartDate().isBefore(today)) {
                throw new GoalException(GoalExceptionType.GOAL_DATE_IN_PAST);
            }
        }


    }
} 