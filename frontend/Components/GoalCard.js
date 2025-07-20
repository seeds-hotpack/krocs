import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { Feather } from '@expo/vector-icons';

export default function GoalCard({ color, progress, title, subGoals = [], completed }) {
  const cardColors = {
    red: '#EF4444',
    yellow: '#FACC15',
    green: '#22C55E',
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
        <Text style={styles.goalDate}>5/10 - 6/10</Text>
        <Text style={styles.goalTitle}>{title}</Text>
        <View style={styles.progressBarBackground}>
          <View
            style={[
              styles.progressBarFill,
              { width: `${progress * 100}%`, backgroundColor: cardColors[color] },
            ]}
          />
        </View>
        {!completed &&
          subGoals.map((goal, idx) => (
            <View key={idx} style={styles.subGoal}>
              <Feather name={goal.done ? 'check-square' : 'square'} size={20} />
              <Text style={styles.subGoalText}>{goal.text}</Text>
              <Feather name="plus" size={16} style={{ marginLeft: 'auto' }} />
            </View>
          ))}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
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
});
