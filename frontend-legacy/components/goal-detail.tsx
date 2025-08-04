"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Checkbox } from "@/components/ui/checkbox"
import { ArrowLeft, Plus, Trash2, Calendar, Clock, Target } from "lucide-react"

interface SubGoal {
  subGoalId: number
  title: string
  completed: boolean
}

interface Goal {
  goalId: number
  title: string
  priority: "LOW" | "MEDIUM" | "HIGH"
  startDate: string
  endDate: string
  duration: number
  completed: boolean
  subGoals: SubGoal[]
  createdAt: string
  updatedAt: string
}

interface GoalDetailProps {
  goal: Goal
  onBack: () => void
  onUpdate: (updatedGoal: Partial<Goal>) => void
}

export function GoalDetail({ goal, onBack, onUpdate }: GoalDetailProps) {
  const [newSubGoalTitle, setNewSubGoalTitle] = useState("")
  const [subGoals, setSubGoals] = useState<SubGoal[]>(goal.subGoals)

  const addSubGoal = () => {
    if (newSubGoalTitle.trim()) {
      const newSubGoal: SubGoal = {
        subGoalId: Date.now(),
        title: newSubGoalTitle.trim(),
        completed: false,
      }
      const updatedSubGoals = [...subGoals, newSubGoal]
      setSubGoals(updatedSubGoals)
      onUpdate({ subGoals: updatedSubGoals })
      setNewSubGoalTitle("")
    }
  }

  const toggleSubGoal = (subGoalId: number) => {
    const updatedSubGoals = subGoals.map((sg) =>
      sg.subGoalId === subGoalId ? { ...sg, completed: !sg.completed } : sg,
    )
    setSubGoals(updatedSubGoals)
    onUpdate({ subGoals: updatedSubGoals })
  }

  const deleteSubGoal = (subGoalId: number) => {
    const updatedSubGoals = subGoals.filter((sg) => sg.subGoalId !== subGoalId)
    setSubGoals(updatedSubGoals)
    onUpdate({ subGoals: updatedSubGoals })
  }

  const toggleGoalCompletion = () => {
    onUpdate({ completed: !goal.completed })
  }

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case "HIGH":
        return "destructive"
      case "MEDIUM":
        return "default"
      case "LOW":
        return "secondary"
      default:
        return "default"
    }
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("ko-KR")
  }

  const completedSubGoals = subGoals.filter((sg) => sg.completed).length
  const progressPercentage = subGoals.length > 0 ? (completedSubGoals / subGoals.length) * 100 : 0

  return (
    <div className="min-h-screen bg-background p-4 md:p-6">
      <div className="max-w-4xl mx-auto">
        <div className="flex items-center gap-4 mb-6">
          <Button variant="ghost" onClick={onBack} className="flex items-center gap-2">
            <ArrowLeft className="h-4 w-4" />
            목록으로
          </Button>
        </div>

        <Card className="mb-6">
          <CardHeader>
            <div className="flex items-start justify-between">
              <div className="space-y-2">
                <CardTitle className={`text-2xl ${goal.completed ? "line-through" : ""}`}>{goal.title}</CardTitle>
                <div className="flex items-center gap-2">
                  <Badge variant={getPriorityColor(goal.priority)}>{goal.priority}</Badge>
                  {goal.completed && (
                    <Badge variant="outline" className="text-green-600">
                      완료
                    </Badge>
                  )}
                </div>
              </div>
              <Button variant={goal.completed ? "secondary" : "default"} onClick={toggleGoalCompletion}>
                {goal.completed ? "완료 취소" : "완료 표시"}
              </Button>
            </div>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="flex items-center gap-2 text-muted-foreground">
                <Calendar className="h-4 w-4" />
                <span>시작일: {formatDate(goal.startDate)}</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground">
                <Calendar className="h-4 w-4" />
                <span>종료일: {formatDate(goal.endDate)}</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground">
                <Clock className="h-4 w-4" />
                <span>기간: {goal.duration}일</span>
              </div>
              <div className="flex items-center gap-2 text-muted-foreground">
                <Target className="h-4 w-4" />
                <span>진행률: {Math.round(progressPercentage)}%</span>
              </div>
            </div>

            {subGoals.length > 0 && (
              <div className="w-full bg-muted rounded-full h-2">
                <div
                  className="bg-primary h-2 rounded-full transition-all duration-300"
                  style={{ width: `${progressPercentage}%` }}
                />
              </div>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Target className="h-5 w-5" />
              하위 목표
            </CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex gap-2">
              <Input
                placeholder="새 하위 목표를 입력하세요"
                value={newSubGoalTitle}
                onChange={(e) => setNewSubGoalTitle(e.target.value)}
                onKeyDown={(e) => {
                  if (e.key === "Enter") {
                    addSubGoal()
                  }
                }}
              />
              <Button onClick={addSubGoal} disabled={!newSubGoalTitle.trim()}>
                <Plus className="h-4 w-4" />
              </Button>
            </div>

            {subGoals.length === 0 ? (
              <div className="text-center py-8 text-muted-foreground">
                <Target className="h-12 w-12 mx-auto mb-4 opacity-50" />
                <p>하위 목표가 없습니다</p>
                <p className="text-sm">위에서 새 하위 목표를 추가해보세요</p>
              </div>
            ) : (
              <div className="space-y-2">
                {subGoals.map((subGoal) => (
                  <div
                    key={subGoal.subGoalId}
                    className="flex items-center gap-3 p-3 rounded-lg border bg-card hover:bg-accent/50 transition-colors"
                  >
                    <Checkbox checked={subGoal.completed} onCheckedChange={() => toggleSubGoal(subGoal.subGoalId)} />
                    <span className={`flex-1 ${subGoal.completed ? "line-through text-muted-foreground" : ""}`}>
                      {subGoal.title}
                    </span>
                    <Button variant="ghost" size="sm" onClick={() => deleteSubGoal(subGoal.subGoalId)}>
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                ))}
              </div>
            )}

            {subGoals.length > 0 && (
              <div className="text-sm text-muted-foreground text-center pt-4 border-t">
                완료된 하위 목표: {completedSubGoals}/{subGoals.length}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
