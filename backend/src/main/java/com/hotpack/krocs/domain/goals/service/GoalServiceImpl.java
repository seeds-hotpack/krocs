package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.converter.GoalConverter;
import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.dto.request.GoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.GoalUpdateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalCreateResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.facade.GoalRepositoryFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalServiceImpl implements GoalService {

    private final GoalRepositoryFacade goalRepositoryFacade;
    private final GoalConverter goalConvertor;
    private final GoalValidator goalValidator;

    @Override
    @Transactional
    public GoalCreateResponseDTO createGoal(GoalCreateRequestDTO requestDTO, Long userId) {
        try {
            goalValidator.validateGoalCreation(requestDTO);

            if (goalRepositoryFacade.existsByTitle(requestDTO.getTitle())) {
                throw new GoalException(GoalExceptionType.GOAL_DUPLICATE_TITLE);
            }

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
    public List<GoalResponseDTO> getGoalByUser(Long userId, LocalDate date) {
        try{
            List<Goal> goals;
            if (date != null) {
                goals = goalRepositoryFacade.findGoalByDate(date);
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
            goalValidator.validateGoalIdParameter(goalId);

            Goal existingGoal = goalRepositoryFacade.findById(goalId);
            if (existingGoal == null) {
                throw new GoalException(GoalExceptionType.GOAL_NOT_FOUND);
            }

            return goalConvertor.toGoalResponseDTO(existingGoal);
        }catch (GoalException e) {
            throw e;
        }catch (Exception e) {
            log.error("대목표 조회 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new GoalException(GoalExceptionType.GOAL_FOUND_FAILED);
        }
    }

    @Override
    @Transactional
    public GoalResponseDTO updateGoalById(Long goalId, GoalUpdateRequestDTO request, Long userId) {
        try {
            goalValidator.validateGoalIdParameter(goalId);

            Goal existingGoal = goalRepositoryFacade.findById(goalId);
            if (existingGoal == null) {
                throw new GoalException(GoalExceptionType.GOAL_NOT_FOUND);
            }

            if (request.getTitle() != null) {
                goalValidator.validateTitle(request.getTitle());
                if (goalRepositoryFacade.existsByTitleAndGoalIdNot(request.getTitle(), goalId)) {
                    throw new GoalException(GoalExceptionType.GOAL_DUPLICATE_TITLE);
                }
            }

            goalValidator.validateDuration(request.getDuration());

            goalValidator.validateUpdateDates(request, existingGoal);

            Goal updatedGoal = Goal.builder()
                    .goalId(existingGoal.getGoalId())
                    .title(request.getTitle() != null ? request.getTitle() : existingGoal.getTitle())
                    .priority(request.getPriority() != null ? request.getPriority() : existingGoal.getPriority())
                    .startDate(request.getStartDate() != null ? request.getStartDate() : existingGoal.getStartDate())
                    .endDate(request.getEndDate() != null ? request.getEndDate() : existingGoal.getEndDate())
                    .duration(request.getDuration() != null ? request.getDuration() : existingGoal.getDuration())
                    .isCompleted(existingGoal.getIsCompleted())
                    .subGoals(existingGoal.getSubGoals())
                    .build();

            Goal savedGoal = goalRepositoryFacade.updateGoal(updatedGoal);
            return goalConvertor.toGoalResponseDTO(savedGoal);
        } catch (GoalException e) {
            throw e;
        } catch (Exception e) {
            log.error("대목표 수정 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new GoalException(GoalExceptionType.GOAL_UPDATE_FAILED);
        }
    }

    @Override
    @Transactional
    public void deleteGoal(Long userId, Long goalId) {
        try {
            goalValidator.validateGoalIdParameter(goalId);
            if (!goalRepositoryFacade.existsById(goalId)) {
                throw new GoalException(GoalExceptionType.GOAL_NOT_FOUND);
            }

            goalRepositoryFacade.deleteGoal(goalId);
        } catch (GoalException e) {
            throw e;
        } catch (Exception e) {
            log.error("대목표 삭제 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new GoalException(GoalExceptionType.GOAL_DELETE_FAILED);
        }
    }
} 