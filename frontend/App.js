import React, { useState } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
  TextInput,
} from 'react-native';
import { Feather } from '@expo/vector-icons';
import AddButton from './Components/AddButton';
import BottomNav from './Components/BottomNav';
import CategoryTabs from './Components/CategoryTabs';
import Header from './Components/Header';
// import GoalCard from './components/GoalCard';
// import DateTimePicker from '@react-native-community/datetimepicker';
// import DateRangePicker from './Components/DateRangePicker';


export default function App() {
  const [showGoalForm, setShowGoalForm] = useState(false);
  const [editingIndex, setEditingIndex] = useState(null);
  const [goals, setGoals] = useState([]);
  const [activeTab, setActiveTab] = useState('goal');
  const [activeCategory, setActiveCategory] = useState('progress');

  //템플릿 저장 스테이트
  const [templates, setTemplates] = useState([]);
  const saveTemplate = (template) => {
    setTemplates(prev => [...prev, template]);
  };
  //템플릿 수정 state
  const [editingGoal, setEditingGoal] = useState(null);
  
  const handleEditTemplate = (index) => {

  const templateToEdit = templates[index];
  // 1. 템플릿에서 삭제
  setTemplates(prev => prev.filter((_, i) => i !== index));
  // 2. 편집 상태에 넣기 (isTemplate: false로 변경)
  setEditingGoal({ ...templateToEdit, isTemplate: false });
  // 3. 폼 열기
  setEditingIndex(null); // 템플릿이라 index 의미 없음
  setShowGoalForm(true);
};

const updateTemplate = (updatedTemplate) => {
    if (editingIndex !== null) {
      setTemplates(prev => {
        const newTemplates = [...prev];
        newTemplates[editingIndex] = updatedTemplate;
        return newTemplates;
      });
    } else {
      setTemplates(prev => [...prev, updatedTemplate]);
    }
  };

  const deleteGoal = (indexToDelete) => {
  setGoals((prev) => prev.filter((_, i) => i !== indexToDelete));
  setEditingIndex(null);
  setShowGoalForm(false);
  };

  const addGoal = (newGoal) => {
    setGoals(prev => [...prev, newGoal]);
  };

  const handleCompleteEarly = (index) => {
  setGoals((prevGoals) => {
    const updatedGoals = [...prevGoals];
    if (!updatedGoals[index].completed) {
      updatedGoals[index] = {
        ...updatedGoals[index],
        completed: true,
        progress: 1,
      };
    }
    return updatedGoals;
  });
};

  const handleEditGoal = (index) => {
  setEditingIndex(index);
  setEditingGoal(goals[index]);
  setShowGoalForm(true); // ✅ 올바르게 연결
};

  return (
    <SafeAreaView style={styles.container}>
      {/* Header */}
      <Header styles = {styles}/>
      {/* Tabs */}
      <View style={styles.tabContainer}>
        <TouchableOpacity
          style={activeTab === 'goal' ? styles.activeTab : styles.inactiveTab}
          onPress={() => setActiveTab('goal')}
        >
          <Text style={activeTab === 'goal' ? styles.activeTabText : styles.inactiveTabText}>목표</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={activeTab === 'timeline' ? styles.activeTab : styles.inactiveTab}
          onPress={() => setActiveTab('timeline')}
        >
          <Text style={activeTab === 'timeline' ? styles.activeTabText : styles.inactiveTabText}>타임라인</Text>
        </TouchableOpacity>
      </View>

      {/* Main Content */}
      {activeTab === 'goal' ? (
  <>
    {/* 카테고리 탭 */}
    <CategoryTabs
      styles={styles}
      activeCategory={activeCategory}
      onCategoryChange={setActiveCategory}
    />

    {/* 카테고리 콘텐츠 전환 */}
   <ScrollView style={styles.goalList}>
  {activeCategory === 'progress' && (
    <>
      {goals.length === 0 ? (
        <Text style={{ padding: 20, fontSize: 16, color: '#888' }}>
          아직 등록된 목표가 없습니다.
        </Text>
      ) : (
        goals.filter(goal => !goal.completed) // 완료 안 된 목표만 보여줌
        .map((goal) => {
  const originalIndex = goals.findIndex(g => g === goal); // 원래 goals 배열에서의 인덱스
  return (
    <GoalCard
      key={originalIndex}
      color={goal.color}
      progress={goal.progress}
      title={goal.title}
      subGoals={goal.subGoals}
      completed={goal.completed}
      startDate={goal.startDate}
      endDate={goal.endDate}
      onCompleteEarly={() => handleCompleteEarly(originalIndex)}
      onEdit={() => handleEditGoal(originalIndex)}
    />
  );
})
      )}
    </>
  )}

  {activeCategory === 'done' && (
    <>
      {goals.filter(goal => goal.completed).length === 0 ? (
        <Text style={{ padding: 20, fontSize: 16, color: '#888' }}>
          완료된 목표가 없습니다.
        </Text>
      ) : (
        goals
          .filter(goal => goal.completed)
          .map((goal, idx) => (
            <GoalCard
              key={idx}
              color={goal.color}
              progress={goal.progress}
              title={goal.title}
              subGoals={goal.subGoals}
              completed={true}
              startDate={goal.startDate}
              endDate={goal.endDate}
            />
          ))
      )}
    </>
  )}

   {activeCategory === 'template' && (
  <ScrollView style={{ padding: 20 }}>
    {templates.length === 0 ? (
      <Text style={{ fontSize: 16, color: '#888' }}>
        저장된 템플릿이 없습니다.
      </Text>
    ) : (
      templates.map((tpl, idx) => (
  <GoalCard
    key={idx}
    color={tpl.color}
    progress={tpl.progress}
    title={tpl.title}
    subGoals={tpl.subGoals}
    completed={tpl.completed}
    startDate={tpl.startDate}
    endDate={tpl.endDate}
    onEdit={() => handleEditTemplate(idx)}
    isTemplate={true}
  />
))
    )}
  </ScrollView>
)}
</ScrollView>
  </>
) : (
  // 타임라인 탭
  <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
    <Text style={{ fontSize: 18 }}>타임라인 화면입니다.</Text>
  </View>
)}
  <AddButton
    styles={styles}
    onAddGoal={addGoal}
    // ✅ 템플릿/목표 모두 editingGoal에 들어오므로 이렇게 수정!
    editingGoal={editingGoal}

    // ✅ 목표 수정 시 사용하는 콜백
    onUpdateGoal={(updatedGoal) => {
    if (editingIndex === null) {
      // 안전장치: editingIndex가 없으면 무시하거나 새로 추가
      setGoals(prev => [...prev, updatedGoal]);
    } else {
      const newGoals = [...goals];
      newGoals[editingIndex] = updatedGoal;
      setGoals(newGoals);
    }
    setEditingIndex(null);
    setEditingGoal(null);
  }}
    onDeleteGoal={deleteGoal}
    showGoalForm={showGoalForm}
    setShowGoalForm={setShowGoalForm}
    goals={goals}
    setGoals={setGoals}
    editingIndex={editingIndex}
    setEditingIndex={setEditingIndex}
    templates={templates}

    // ✅ 이 부분이 핵심: updateTemplate 함수를 넘겨야 합니다!
    saveTemplate={updateTemplate}
    setEditingGoal={setEditingGoal}
  />
  {/* <AddButton
  styles={styles}
  onAddGoal={addGoal}
  editingGoal={editingIndex !== null ? goals[editingIndex] : null}
  onUpdateGoal={(updatedGoal) => {
    const newGoals = [...goals];
    newGoals[editingIndex] = updatedGoal;
    setGoals(newGoals);
    setEditingIndex(null); // 수정 상태 초기화
  }}
  onDeleteGoal={deleteGoal}
  showGoalForm={showGoalForm}
  setShowGoalForm={setShowGoalForm}
  goals={goals}
  setGoals={setGoals}
  editingIndex={editingIndex}
  setEditingIndex={setEditingIndex}
  templates={templates}          
  saveTemplate={saveTemplate}
/> */}
      {/* Bottom Navigation */}
      <BottomNav styles={styles}/>
    </SafeAreaView>
  );
}

const GoalCard = ({ color, progress, title, subGoals = [], completed, startDate, endDate,onCompleteEarly,onEdit, }) => {
  const cardColors = {
    red: '#EF4444',
    yellow: '#FACC15',
    green: '#22C55E',
  };

  const formatMonthDay = (dateStr) => {
  // "YYYY.MM.DD" → "M월 D일" 로 변환
  const [, month, day] = dateStr.split('.');
  return `${parseInt(month)}월 ${parseInt(day)}일`;
};

  return (
    <View style={styles.goalCard}>
      <View style={[styles.colorStrip, { backgroundColor: cardColors[color] }]}>
        {completed ? (
          <Feather name="check" size={20} color="white" />
        ) : (
          <Feather name="chevron-up" size={20} color="white" />
        )}
      </View>
      <View style={styles.goalContent}>
        <Text style={styles.goalDate}>{formatMonthDay(startDate)} - {formatMonthDay(endDate)}</Text>
        <Text style={styles.goalTitle}>{title}</Text>
        <View style={styles.progressBarBackground}>
          <View style={[styles.progressBarFill, { width: `${progress * 100}%`, backgroundColor: cardColors[color] }]} />
        </View>
        {!completed &&
          subGoals.map((goal, idx) => (
            <View
  key={idx}
  style={{
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    marginVertical: 4,
  }}
>
  {/* 왼쪽: 체크박스 + 텍스트 */}
  <View style={{ flexDirection: 'row', alignItems: 'center' }}>
    <TouchableOpacity onPress={() => {
      const updated = [...subGoals];
      updated[idx] = { ...updated[idx], done: !updated[idx].done };
      setSubGoals(updated);
    }}>
      <Feather name={goal.done ? 'check-square' : 'square'} size={20} />
    </TouchableOpacity>

    <Text style={{ marginLeft: 8 }}>{goal.text}</Text>
  </View>

  {/* 오른쪽: ✕ 버튼 */}
  <TouchableOpacity onPress={() => {
    const updated = [...subGoals];
    updated.splice(idx, 1);
    setSubGoals(updated);
  }}>
    <Text style={{ fontSize: 16, color: '#999' }}>✕</Text>
  </TouchableOpacity>
</View>
))}
          <View style={{ flexDirection: 'row', justifyContent: 'flex-end', marginTop: 10 }}>
  {!completed && (
    <TouchableOpacity
      onPress={onCompleteEarly}
      style={{
        backgroundColor: 'green',
        paddingVertical: 6,
        paddingHorizontal: 12,
        borderRadius: 6,
        marginRight: 10,
      }}
    >
      <Text style={{ color: 'white', fontWeight: 'bold' }}>조기 완료</Text>
    </TouchableOpacity>
  )}
  <TouchableOpacity
    onPress={onEdit}
    style={{
      backgroundColor: '#3B82F6',
      paddingVertical: 6,
      paddingHorizontal: 12,
      borderRadius: 6,
    }}
  >
    <Text style={{ color: 'white', fontWeight: 'bold' }}>수정</Text>
  </TouchableOpacity>
</View>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#fff' },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    padding: 16,
    backgroundColor: '#1F2937',
  },
  logoBox: {
    backgroundColor: '#374151',
    padding: 12,
    borderRadius: 6,
  },
  logoText: { color: 'white', fontWeight: 'bold' },
  menuIcon: { padding: 10 },

  tabContainer: { flexDirection: 'row', justifyContent: 'center', padding: 8 },
  activeTab: {
    backgroundColor: '#1F2937',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
    marginRight: 8,
  },
  inactiveTab: {
    borderWidth: 2,
    borderColor: '#1F2937',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
  },
  activeTabText: { color: 'white', fontWeight: 'bold' },
  inactiveTabText: { color: '#1F2937' },

  categoryTabs: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    paddingVertical: 12,
    borderBottomWidth: 1,
    borderBottomColor: '#ccc',
  },
  selectedCategory: {
    fontWeight: 'bold',
    borderBottomWidth: 4,
    borderBottomColor: '#3B82F6',
  },
  category: { color: '#444' },

  goalList: { paddingHorizontal: 12, marginTop: 10 },

  goalCard: {
    flexDirection: 'row',
    backgroundColor: '#D1D5DB',
    borderRadius: 12,
    marginBottom: 12,
    overflow: 'hidden',
  },
  colorStrip: {
    width: 40,
    justifyContent: 'center',
    alignItems: 'center',
  },
  goalContent: { flex: 1, padding: 10 },
  goalDate: { color: '#555', fontSize: 12 },
  goalTitle: { fontSize: 16, fontWeight: 'bold', marginVertical: 4 },
  progressBarBackground: {
    height: 12,
    backgroundColor: '#eee',
    borderRadius: 6,
    overflow: 'hidden',
    marginVertical: 8,
  },
  progressBarFill: {
    height: '100%',
    borderRadius: 6,
  },
  subGoal: {
    flexDirection: 'row',
    alignItems: 'center',
    marginTop: 6,
  },
  subGoalText: { marginLeft: 8, fontSize: 14 },

  addButton: {
    position: 'absolute',
    bottom: 80,
    right: 20,
    backgroundColor: 'black',
    width: 56,
    height: 56,
    borderRadius: 28,
    justifyContent: 'center',
    alignItems: 'center',
  },
  addButtonText: { color: 'white', fontSize: 28 },

  bottomNav: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    padding: 14,
    borderTopWidth: 1,
    borderColor: '#ccc',
  },
  navItem: { color: '#999' },

  goalForm: {
  position: 'absolute',
  top: 80,
  left: 20,
  right: 20,
  backgroundColor: 'white',
  padding: 16,
  borderRadius: 12,
  elevation: 5,
  shadowColor: '#000',
  shadowOpacity: 0.2,
  shadowRadius: 6,
},
goalFormHeader: {
  flexDirection: 'row',
  justifyContent: 'space-between',
  alignItems: 'center',
  marginBottom: 10,
},
goalFormTitle: {
  fontWeight: 'bold',
  fontSize: 18,
},
input: {
  borderWidth: 1,
  borderColor: '#ccc',
  borderRadius: 6,
  padding: 8,
  marginBottom: 10,
},
dateInput: {
  flex: 1,
  borderWidth: 1,
  borderColor: '#ccc',
  padding: 8,
  borderRadius: 6,
  marginHorizontal: 4,
},
row: {
  flexDirection: 'row',
  justifyContent: 'space-around',
  marginVertical: 6,
},
smallButton: {
  borderWidth: 1,
  borderColor: '#444',
  paddingHorizontal: 10,
  paddingVertical: 6,
  borderRadius: 6,
  marginHorizontal: 2,
},
subGoalList: {
  marginTop: 8,
  borderWidth: 1,
  borderRadius: 8,
  padding: 10,
},
subGoalItem: {
  flexDirection: 'row',
  justifyContent: 'space-between',
  marginBottom: 6,
},
saveButton: {
  backgroundColor: 'black',
  padding: 10,
  borderRadius: 6,
  marginTop: 14,
  alignItems: 'center',
},
customDaysModal: {
  position: 'absolute',
  top: '35%',
  left: '10%',
  width: '80%',
  padding: 20,
  backgroundColor: 'white',
  borderRadius: 10,
  zIndex: 9999,           // iOS
  elevation: 10,          // Android
  shadowColor: '#000',
  shadowOffset: { width: 0, height: 2 },
  shadowOpacity: 0.3,
  shadowRadius: 4,
},
modalInput: {
  borderWidth: 1,
  borderColor: '#ccc',
  borderRadius: 6,
  padding: 10,
  marginBottom: 12,
},

modalButtonContainer: {
  flexDirection: 'row',
  justifyContent: 'flex-end',
},

modalButton: {
  marginLeft: 10,
},
});