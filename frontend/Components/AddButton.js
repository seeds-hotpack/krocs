import React, { useState, useEffect } from 'react'
import {
  View,
  Text,
  TouchableOpacity,
  TextInput,
  ScrollView,
  Platform,
  Keyboard, 
  TouchableWithoutFeedback,
} from 'react-native';
import { Feather } from '@expo/vector-icons';
import DateTimePicker from '@react-native-community/datetimepicker';

export default function AddButton({   styles, 
  onAddGoal, 
  editingGoal, 
  onUpdateGoal, 
  onDeleteGoal,
  showGoalForm, 
  setShowGoalForm,
  goals,
  setGoals,
  editingIndex,       // 👈 여기에 추가
  setEditingIndex,}) {

  // 세부 목표 추가 
  const [title, setTitle] = useState('');
  const [subGoals, setSubGoals] = useState([]);
  const [subGoalModalVisible, setSubGoalModalVisible] = useState(false);
  const [newSubGoalText, setNewSubGoalText] = useState('');



  // 3,7,30,+ 부분 기능 구현 
  const [isStartDateTouched, setIsStartDateTouched] = useState(false);

  const [customDaysModalVisible, setCustomDaysModalVisible] = useState(false);
  const [customDaysInput, setCustomDaysInput] = useState(''); // 직접 + 하면 날짜를 더하기위한 state 부분

  const addDays = (date, days) => {
  const result = new Date(date);
  result.setDate(result.getDate() + days);
  return result;
};
const resetGoalForm = () => {
  setShowGoalForm(false);
  setEditingIndex(null);
  setTitle('');
  setSubGoals([]);
  setStartDate(new Date());
  setEndDate(addDays(new Date(), 1));
};
const handleDurationSelect = (days) => {
  const baseDate = isStartDateTouched ? startDate : new Date();
  const newStartDate = isStartDateTouched ? startDate : new Date();
  const newEndDate = addDays(baseDate, days);

  setStartDate(newStartDate);
  setEndDate(newEndDate);

  if (!isStartDateTouched) {
    setIsStartDateTouched(true); // 날짜를 자동으로라도 설정한 걸로 처리
  }
};
// 여기 까지 관련된 것들 

  // 날짜 상태(Date 객체)
  const [startDate, setStartDate] = useState(new Date()); // ⬅ 오늘 날짜
  const [endDate, setEndDate] = useState(addDays(new Date(), 1));

  // 어떤 날짜 선택중인지 ('start' or 'end' or null)
  const [pickerMode, setPickerMode] = useState(null);

  // DatePicker 보여줄지 여부
  const [showPicker, setShowPicker] = useState(false);

  // 날짜 포맷 함수 (YYYY.MM.DD)
  const formatDate = (date) => {
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${y}.${m}.${d}`;
  };

  // DatePicker 변경 핸들러
  const onChange = (event, selectedDate) => {
  // iOS에서 취소할 때 selectedDate는 undefined임
  setShowPicker(false); // 선택하든 취소하든 무조건 닫음

  if (selectedDate) {
    if (pickerMode === 'start') {
      setStartDate(selectedDate);
      if (selectedDate > endDate) {
        setEndDate(selectedDate);
      }
    } else if (pickerMode === 'end') {
      if (selectedDate >= startDate) {
        setEndDate(selectedDate);
      } else {
        alert('종료일은 시작일 이후여야 합니다.');
      }
    }
  }
  setPickerMode(null);
};


//목표 수정을  하기위한 코드들
useEffect(() => {
  if (editingGoal) {
    setTitle(editingGoal.title);
    setStartDate(parseDate(editingGoal.startDate));
    setEndDate(parseDate(editingGoal.endDate));
    setSubGoals(editingGoal.subGoals.map(s => s.text));
    setShowGoalForm(true);
  }
}, [editingGoal]);

const parseDate = (str) => {
  const [y, m, d] = str.split('.').map(Number);
  return new Date(y, m - 1, d);
};
// 애는 초기화 시 오늘 날짜를 유지하는 코드
useEffect(() => {
  if (showGoalForm && !editingGoal) {
    const today = new Date();
    setStartDate(today);
    setEndDate(addDays(today, 1));
  }
}, [showGoalForm]);
// 여기 까지

const saveGoal = () => {
    if (!title.trim()) {
      alert('목표명을 입력하세요.');
      return;
    }

    const newGoal = {
  title,
  startDate: formatDate(startDate),
  endDate: formatDate(endDate),
  subGoals: subGoals.map(text => ({ text, done: false })),
  completed: editingGoal ? editingGoal.completed : false,
  progress: editingGoal ? editingGoal.progress : 0,
  color: 'red',
};
    if (editingGoal) {
    onUpdateGoal(newGoal); // 👉 기존 목표 수정
  } else {
    onAddGoal(newGoal);    // 👉 새 목표 추가
  }

    // 초기화 후 닫기
    setTitle('');
    setSubGoals([]);
    setShowGoalForm(false);
  };

  return (
    <>
      <TouchableOpacity
        style={styles.addButton}
        onPress={() => setShowGoalForm(true)}
      >
        <Text style={styles.addButtonText}>＋</Text>
      </TouchableOpacity>

      {showGoalForm && (
         <TouchableWithoutFeedback onPress={() => Keyboard.dismiss()}>
        <View style={styles.goalForm}>
          {customDaysModalVisible && (
            <View style={styles.customDaysModal}>
              <Text style={{ marginBottom: 10 }}>며칠 후까지 설정할까요?</Text>
              <TextInput
              value={customDaysInput}
              onChangeText={setCustomDaysInput}
              keyboardType="numeric"
              placeholder="숫자만 입력"
              style={styles.modalInput}
              />
              <View style={styles.modalButtonContainer}>
                <TouchableOpacity
                onPress={() => setCustomDaysModalVisible(false)}
                style={{ marginRight: 10 }}>
                  <Text>취소</Text>
                  </TouchableOpacity>
      <TouchableOpacity
        onPress={() => {
          const days = parseInt(customDaysInput, 10);
          if (!isNaN(days)) {
            handleDurationSelect(days);
            setCustomDaysInput('');
            setCustomDaysModalVisible(false);
          } else {
            alert('올바른 숫자를 입력하세요.');
          }
        }}
        style={styles.modalButton}
      >
        <Text>확인</Text>
      </TouchableOpacity>
    </View>
  </View>
)}
          <View style={styles.goalFormHeader}>
            <Text style={styles.goalFormTitle}>새로운 목표</Text>
            <TouchableOpacity onPress={resetGoalForm}>
              <Text style={{ fontSize: 18 }}>✕</Text>
            </TouchableOpacity>
          </View>

          <TextInput
  style={styles.input}
  placeholder="목표명"
  value={title}
  onChangeText={setTitle}
/>

          {/* 날짜 선택 컴포넌트 */}
          <View style={[styles.row, { alignItems: 'center', flexWrap: 'nowrap' }]}>
            <Feather name="calendar" size={20} color="#444" style={{ marginRight: 8 }} />

            <TouchableOpacity
              style={{
                borderWidth: 1,
                borderColor: '#ccc',
                borderRadius: 6,
                paddingVertical: 6,
                paddingHorizontal: 10,
                marginRight: 6,
                width: 120,
                justifyContent: 'center',
                alignItems: 'center',
              }}
              onPress={() => {
                setIsStartDateTouched(true); // 👉 사용자가 직접 선택함
                setPickerMode('start');
                setShowPicker(true);
              }}
            >
              <Text style={{ fontSize: 14, color: '#444' }}>{formatDate(startDate)}</Text>
            </TouchableOpacity>

            <Text style={{ marginHorizontal: 4 }}>→</Text>

            <TouchableOpacity
              style={{
                borderWidth: 1,
                borderColor: '#ccc',
                borderRadius: 6,
                paddingVertical: 6,
                paddingHorizontal: 10,
                width: 120,
                justifyContent: 'center',
                alignItems: 'center',
              }}
              onPress={() => {
                setPickerMode('end');
                setShowPicker(true);
              }}
            >
              <Text style={{ fontSize: 14, color: '#444' }}>{formatDate(endDate)}</Text>
            </TouchableOpacity>
          </View>

          {/* DateTimePicker 모달 */}
          {showPicker && (
            <DateTimePicker
              value={pickerMode === 'start' ? startDate : endDate}
              mode="date"
              display="default"
              onChange={onChange}
              minimumDate={pickerMode === 'end' ? startDate : undefined}
            />
          )}

          {/* 기존 UI 계속 유지 */}
          <View style={styles.row}>
  <TouchableOpacity style={styles.smallButton} onPress={() => handleDurationSelect(3)}>
    <Text>3</Text>
  </TouchableOpacity>
  <TouchableOpacity style={styles.smallButton} onPress={() => handleDurationSelect(7)}>
    <Text>7</Text>
  </TouchableOpacity>
  <TouchableOpacity style={styles.smallButton} onPress={() => handleDurationSelect(30)}>
    <Text>30</Text>
  </TouchableOpacity>
  <TouchableOpacity style={styles.smallButton} onPress={() => setCustomDaysModalVisible(true)}>
    <Text>＋</Text>
  </TouchableOpacity>
</View>

          <View style={styles.row}>
            <Feather name="repeat" size={20} color="#888" style={{ marginRight: 6, alignSelf: 'center' }} />
            <TouchableOpacity style={styles.smallButton}><Text>매일</Text></TouchableOpacity>
            <TouchableOpacity style={styles.smallButton}><Text>일주일</Text></TouchableOpacity>
            <TouchableOpacity style={styles.smallButton}><Text>한달</Text></TouchableOpacity>
            <TouchableOpacity style={styles.smallButton}><Text>＋</Text></TouchableOpacity>
          </View>

          <View style={styles.row}>
            <TouchableOpacity style={styles.smallButton}><Text>5</Text></TouchableOpacity>
            <TouchableOpacity style={[styles.smallButton, { backgroundColor: '#ccc' }]}><Text>10</Text></TouchableOpacity>
            <TouchableOpacity style={styles.smallButton}><Text>30</Text></TouchableOpacity>
            <TouchableOpacity style={styles.smallButton}><Text>1h</Text></TouchableOpacity>
            <TouchableOpacity style={styles.smallButton}><Text>...</Text></TouchableOpacity>
          </View>

          <View style={[styles.row, { alignItems: 'center', marginTop: 12 }]}>
            <Feather name="scissors" size={16} color="#444" style={{ marginRight: 8 }} />

            <View style={{ flexDirection: 'row', alignItems: 'center' }}>
              <View style={{ flexDirection: 'row', alignItems: 'center', marginRight: 10 }}>
                <View style={{
                  width: 16,
                  height: 16,
                  borderRadius: 8,
                  borderWidth: 2,
                  borderColor: '#444',
                  justifyContent: 'center',
                  alignItems: 'center',
                  marginRight: 6,
                }}>
                  <View style={{
                    width: 8,
                    height: 8,
                    borderRadius: 4,
                    backgroundColor: '#444',
                  }} />
                </View>
                <Text>정량 입력</Text>
              </View>

              <TextInput
                style={{
                  borderWidth: 1,
                  borderColor: '#444',
                  borderRadius: 6,
                  paddingVertical: 6,
                  paddingHorizontal: 10,
                  backgroundColor: '#fff',
                  width: 100,
                }}
                placeholder="숫자만 입력"
                keyboardType="numeric"
              />
            </View>
          </View>

          <Text style={{ marginTop: 12, fontWeight: 'bold' }}>세부 목표</Text>
<View style={styles.subGoalList}>
  {subGoals.map((text, idx) => (
    <View key={idx} style={styles.subGoalItem}>
      <Text>✅ {text}</Text>
      <TouchableOpacity onPress={() => {
        const updated = [...subGoals];
        updated.splice(idx, 1);
        setSubGoals(updated);
      }}>
        <Text>✕</Text>
      </TouchableOpacity>
    </View>
  ))}
  
  <TouchableOpacity
    style={[styles.smallButton, { alignSelf: 'flex-start', marginTop: 10 }]}
    onPress={() => setSubGoalModalVisible(true)}
  >
    <Text>＋</Text>
  </TouchableOpacity>
</View>

{subGoalModalVisible && (
  <View style={{
    position: 'absolute',
    top: '35%',
    left: '10%',
    width: '80%',
    padding: 20,
    backgroundColor: 'white',
    borderRadius: 10,
    elevation: 10,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.3,
    shadowRadius: 4,
  }}>
    <Text style={{ marginBottom: 10 }}>세부 목표를 입력하세요</Text>
    <TextInput
      value={newSubGoalText}
      onChangeText={setNewSubGoalText}
      placeholder="예: API 명세서 작성"
      style={{
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 6,
        padding: 10,
        marginBottom: 12,
      }}
    />
    <View style={{ flexDirection: 'row', justifyContent: 'flex-end' }}>
      <TouchableOpacity
        onPress={() => setSubGoalModalVisible(false)}
        style={{ marginRight: 10 }}
      >
        <Text>취소</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => {
          if (newSubGoalText.trim()) {
            setSubGoals([...subGoals, newSubGoalText.trim()]);
            setNewSubGoalText('');
            setSubGoalModalVisible(false);
          } else {
            alert('세부 목표를 입력해주세요.');
          }
        }}
      >
        <Text>확인</Text>
      </TouchableOpacity>
    </View>
  </View>
)}

          <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: 14 }}>
            {/* <TouchableOpacity
              style={{
                backgroundColor: '#e11d48',
                width: 48,
                height: 48,
                borderRadius: 6,
                justifyContent: 'center',
                alignItems: 'center',
              }}
              onPress={() => {
  setShowGoalForm(false);            // 폼 닫기
  setStartDate(new Date());          // 시작일 초기화
  setEndDate(new Date());            // 종료일 초기화
  setIsStartDateTouched(false);      // 날짜 선택 여부 초기화
  setCustomDaysInput('');            // 커스텀 일수 입력 초기화
  setSubGoals([]);                   // 세부 목표 리스트 초기화
  setNewSubGoalText('');             // 세부 목표 입력창 초기화
  setTitle('');
}}
            >
              <Feather name="trash-2" size={20} color="white" />
            </TouchableOpacity> */}
            <TouchableOpacity
  style={{
    backgroundColor: '#e11d48',
    width: 48,
    height: 48,
    borderRadius: 6,
    justifyContent: 'center',
    alignItems: 'center',
  }}
  onPress={() => {
    if (editingGoal) {
      // 수정 중이면 삭제 기능
      onDeleteGoal(editingIndex);
      resetGoalForm();

    } else {
      // 새 목표 작성 중이면 창 닫기
      setShowGoalForm(false);
      setStartDate(new Date());
      setEndDate(addDays(new Date(), 1));
      setIsStartDateTouched(false);
      setCustomDaysInput('');
      setSubGoals([]);
      setNewSubGoalText('');
      setTitle('');
    }
  }}
>
  <Feather name="trash-2" size={20} color="white" />
</TouchableOpacity> 

            <TouchableOpacity
              style={{
                backgroundColor: 'black',
                width: 48,
                height: 48,
                borderRadius: 6,
                justifyContent: 'center',
                alignItems: 'center',
              }}
              onPress={saveGoal}
            >
              <Feather name="check" size={20} color="white" />
            </TouchableOpacity>
          </View>
        </View>
        </TouchableWithoutFeedback>
      )}
    </>
  );
}
// import React, { useState } from 'react';
// import {
//   View,
//   Text,
//   TouchableOpacity,
//   TextInput,
//   ScrollView,
// } from 'react-native';
// import { Feather } from '@expo/vector-icons';
// import { Picker } from '@react-native-picker/picker'; 


// export default function AddButton({ styles }) {
//   const [showGoalForm, setShowGoalForm] = useState(false);
//   const [startYear, setStartYear] = useState('2025');
// const [startMonth, setStartMonth] = useState('07');
// const [startDay, setStartDay] = useState('19');

// const [endYear, setEndYear] = useState('2025');
// const [endMonth, setEndMonth] = useState('07');
// const [endDay, setEndDay] = useState('20');

// const years = Array.from({ length: 11 }, (_, i) => `${2020 + i}`);
// const months = Array.from({ length: 12 }, (_, i) => String(i + 1).padStart(2, '0'));
// const days = Array.from({ length: 31 }, (_, i) => String(i + 1).padStart(2, '0'));

//   return (
//     <>
//       <TouchableOpacity
//         style={styles.addButton}
//         onPress={() => setShowGoalForm(true)}
//       >
//         <Text style={styles.addButtonText}>＋</Text>
//       </TouchableOpacity>

//       {showGoalForm && (
//         <View style={styles.goalForm}>
//           <View style={styles.goalFormHeader}>
//             <Text style={styles.goalFormTitle}>새로운 목표</Text>
//             <TouchableOpacity onPress={() => setShowGoalForm(false)}>
//               <Text style={{ fontSize: 18 }}>✕</Text>
//             </TouchableOpacity>
//           </View>

//           <TextInput style={styles.input} placeholder="목표명" />

//           {/* ✅ 날짜 선택 컴포넌트 */}
//        <View style={[styles.row, { alignItems: 'center', flexWrap: 'nowrap' }]}>
//   <Feather
//     name="calendar"
//     size={20}
//     color="#444"
//     style={{ marginRight: 8 }}
//   />

//   {/* 시작 날짜 박스 */}
//   <View style={{
//     borderWidth: 1,
//     borderColor: '#ccc',
//     borderRadius: 6,
//     paddingHorizontal: 6,
//     paddingVertical: 4,
//     marginRight: 6,
//     flexShrink: 1,
//     flexBasis: '40%',
//   }}>
//     <ScrollView horizontal showsHorizontalScrollIndicator={false}>
//       <View style={{ flexDirection: 'row', alignItems: 'center' }}>
//         <Picker
//           selectedValue={startYear}
//           onValueChange={setStartYear}
//           style={{ width: 70, height: 32 }}
//           itemStyle={{ fontSize: 12 }}
//         >
//           {years.map((y) => <Picker.Item key={y} label={y} value={y} />)}
//         </Picker>
//         <Text style={{ fontSize: 12, marginRight: 4 }}>년</Text>

//         <Picker
//           selectedValue={startMonth}
//           onValueChange={setStartMonth}
//           style={{ width: 50, height: 32 }}
//           itemStyle={{ fontSize: 12 }}
//         >
//           {months.map((m) => <Picker.Item key={m} label={m} value={m} />)}
//         </Picker>
//         <Text style={{ fontSize: 12, marginRight: 4 }}>월</Text>

//         <Picker
//           selectedValue={startDay}
//           onValueChange={setStartDay}
//           style={{ width: 50, height: 32 }}
//           itemStyle={{ fontSize: 12 }}
//         >
//           {days.map((d) => <Picker.Item key={d} label={d} value={d} />)}
//         </Picker>
//         <Text style={{ fontSize: 12 }}>일</Text>
//       </View>
//     </ScrollView>
//   </View>

//   <Text style={{ marginHorizontal: 4 }}>→</Text>

//   {/* 종료 날짜 박스 */}
//   <View style={{
//     borderWidth: 1,
//     borderColor: '#ccc',
//     borderRadius: 6,
//     paddingHorizontal: 6,
//     paddingVertical: 4,
//     flexShrink: 1,
//     flexBasis: '40%',
//   }}>
//     <ScrollView horizontal showsHorizontalScrollIndicator={false}>
//       <View style={{ flexDirection: 'row', alignItems: 'center' }}>
//         <Picker
//           selectedValue={endYear}
//           onValueChange={setEndYear}
//           style={{ width: 70, height: 32 }}
//           itemStyle={{ fontSize: 12 }}
//         >
//           {years.map((y) => <Picker.Item key={y} label={y} value={y} />)}
//         </Picker>
//         <Text style={{ fontSize: 12, marginRight: 4 }}>년</Text>

//         <Picker
//           selectedValue={endMonth}
//           onValueChange={setEndMonth}
//           style={{ width: 50, height: 32 }}
//           itemStyle={{ fontSize: 12 }}
//         >
//           {months.map((m) => <Picker.Item key={m} label={m} value={m} />)}
//         </Picker>
//         <Text style={{ fontSize: 12, marginRight: 4 }}>월</Text>

//         <Picker
//           selectedValue={endDay}
//           onValueChange={setEndDay}
//           style={{ width: 50, height: 32 }}
//           itemStyle={{ fontSize: 12 }}
//         >
//           {days.map((d) => <Picker.Item key={d} label={d} value={d} />)}
//         </Picker>
//         <Text style={{ fontSize: 12 }}>일</Text>
//       </View>
//     </ScrollView>
//   </View>
// </View>


//           <View style={styles.row}>
//             <TouchableOpacity style={styles.smallButton}><Text>3</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>7</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>30</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>＋</Text></TouchableOpacity>
//           </View>

//           <View style={styles.row}>
//             <Feather name="repeat" size={20} color="#888" style={{ marginRight: 6, alignSelf: 'center' }} />
//             <TouchableOpacity style={styles.smallButton}><Text>매일</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>일주일</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>한달</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>＋</Text></TouchableOpacity>
//           </View>

//           <View style={styles.row}>
//             <TouchableOpacity style={styles.smallButton}><Text>5</Text></TouchableOpacity>
//             <TouchableOpacity style={[styles.smallButton, { backgroundColor: '#ccc' }]}><Text>10</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>30</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>1h</Text></TouchableOpacity>
//             <TouchableOpacity style={styles.smallButton}><Text>...</Text></TouchableOpacity>
//           </View>

//           <View style={[styles.row, { alignItems: 'center', marginTop: 12 }]}>
//   <Feather name="scissors" size={16} color="#444" style={{ marginRight: 8 }} />

//   {/* 정량 입력 라디오 + 텍스트 + 숫자 입력 */}
//   <View style={{ flexDirection: 'row', alignItems: 'center' }}>
//     {/* 라디오 버튼 + 정량 입력 텍스트 */}
//     <View style={{ flexDirection: 'row', alignItems: 'center', marginRight: 10 }}>
//       <View style={{
//         width: 16,
//         height: 16,
//         borderRadius: 8,
//         borderWidth: 2,
//         borderColor: '#444',
//         justifyContent: 'center',
//         alignItems: 'center',
//         marginRight: 6,
//       }}>
//         <View style={{
//           width: 8,
//           height: 8,
//           borderRadius: 4,
//           backgroundColor: '#444',
//         }} />
//       </View>
//       <Text>정량 입력</Text>
//     </View>

//     <TextInput
//   style={{
//     borderWidth: 1,
//     borderColor: '#444',
//     borderRadius: 6,
//     paddingVertical: 6,
//     paddingHorizontal: 10,
//     backgroundColor: '#fff',
//     width: 100, // 원하는 너비로 조정 가능
//   }}
//   placeholder="숫자만 입력"
//   keyboardType="numeric"
// />
//   </View>
// </View>

//           <Text style={{ marginTop: 12, fontWeight: 'bold' }}>세부 목표</Text>
//           <View style={styles.subGoalList}>
//             <View style={styles.subGoalItem}>
//               <Text>✅ 정보구조도 그리기</Text>
//               <Text>✕</Text>
//             </View>
//             <View style={styles.subGoalItem}>
//               <Text>✅ 피그마 기초 공부하기</Text>
//               <Text>✕</Text>
//             </View>
//             <View style={styles.subGoalItem}>
//               <Text>✅ API 명세서 작성</Text>
//               <Text>✕</Text>
//             </View>
//              <TouchableOpacity style={[styles.smallButton, { alignSelf: 'flex-start', marginTop: 10 }]}>
//             <Text>＋</Text>
//           </TouchableOpacity>

//           </View>

         
//           <View style={{ flexDirection: 'row', justifyContent: 'space-between', marginTop: 14 }}>
//   {/* 삭제(휴지통) 버튼 - 왼쪽에 작게 */}
//   <TouchableOpacity
//     style={{
//       backgroundColor: '#e11d48',
//       width: 48,
//       height: 48,
//       borderRadius: 6,
//       justifyContent: 'center',
//       alignItems: 'center',
//     }}
//     onPress={() => setShowGoalForm(false)}
//   >
//     <Feather name="trash-2" size={20} color="white" />
//   </TouchableOpacity>

//   {/* 저장 버튼 - 오른쪽에 넓게 */}
//   <TouchableOpacity
//     style={{
//       backgroundColor: 'black',
//       width: 48,
//       height: 48,
//       borderRadius: 6,
//       justifyContent: 'center',
//       alignItems: 'center',
//     }}
//     onPress={() => {
//       // 저장 로직 여기에
//     }}
//   >
//     <Feather name="check" size={20} color="white" />
//   </TouchableOpacity>
// </View>
//         </View>
        
//       )}
//     </>
//   );
// }

// const styles = StyleSheet.create({
//   addButton: {
//     position: 'absolute',
//     bottom: 80,
//     right: 20,
//     backgroundColor: 'black',
//     width: 56,
//     height: 56,
//     borderRadius: 28,
//     justifyContent: 'center',
//     alignItems: 'center',
//   },
//   addButtonText: { color: 'white', fontSize: 28 },
// });