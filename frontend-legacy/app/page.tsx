"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Plus, Edit, Trash2, Calendar, Clock, Target } from "lucide-react"
import { GoalForm } from "@/components/goal-form"
import { GoalDetail } from "@/components/goal-detail"

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

export default function GoalManagementApp() {
  const [goals, setGoals] = useState<Goal[]>([])
  const [selectedGoal, setSelectedGoal] = useState<Goal | null>(null)
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [editingGoal, setEditingGoal] = useState<Goal | null>(null)
  const [loading, setLoading] = useState(false)

  // Mock API functions - replace with actual API calls
  const fetchGoals = async () => {
    setLoading(true)
    // Mock data for demonstration
    const mockGoals: Goal[] = [
      {
        goalId: 1,
        title: "프로젝트 완료하기",
        priority: "HIGH",
        startDate: "2025-08-03",
        endDate: "2025-08-31",
        duration: 28,
        completed: false,
        subGoals: [
          { subGoalId: 1, title: "요구사항 분석", completed: true },
          { subGoalId: 2, title: "UI/UX 디자인", completed: false },
        ],
        createdAt: "2025-08-03T05:56:23.681Z",
        updatedAt: "2025-08-03T05:56:23.681Z",
      },
      {
        goalId: 2,
        title: "운동 루틴 만들기",
        priority: "MEDIUM",
        startDate: "2025-08-01",
        endDate: "2025-08-15",
        duration: 14,
        completed: false,
        subGoals: [],
        createdAt: "2025-08-01T05:56:23.681Z",
        updatedAt: "2025-08-01T05:56:23.681Z",
      },
    ]

    setTimeout(() => {
      setGoals(mockGoals)
      setLoading(false)
    }, 1000)
  }

  const createGoal = async (goalData: Omit<Goal, "goalId" | "completed" | "subGoals" | "createdAt" | "updatedAt">) => {
    // Mock API call
    const newGoal: Goal = {
      ...goalData,
      goalId: Date.now(),
      completed: false,
      subGoals: [],
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    }

    setGoals((prev) => [...prev, newGoal])
    setIsFormOpen(false)
  }

  const updateGoal = async (goalId: number, goalData: Partial<Goal>) => {
    setGoals((prev) =>
      prev.map((goal) =>
        goal.goalId === goalId ? { ...goal, ...goalData, updatedAt: new Date().toISOString() } : goal,
      ),
    )
    setEditingGoal(null)
    setIsFormOpen(false)
  }

  const deleteGoal = async (goalId: number) => {
    setGoals((prev) => prev.filter((goal) => goal.goalId !== goalId))
    if (selectedGoal?.goalId === goalId) {
      setSelectedGoal(null)
    }
  }

  const toggleGoalCompletion = async (goalId: number) => {
    setGoals((prev) =>
      prev.map((goal) =>
        goal.goalId === goalId ? { ...goal, completed: !goal.completed, updatedAt: new Date().toISOString() } : goal,
      ),
    )
  }

  useEffect(() => {
    fetchGoals()
  }, [])

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

  if (selectedGoal) {
    return (
      <GoalDetail
        goal={selectedGoal}
        onBack={() => setSelectedGoal(null)}
        onUpdate={(updatedGoal) => {
          updateGoal(selectedGoal.goalId, updatedGoal)
          setSelectedGoal({ ...selectedGoal, ...updatedGoal })
        }}
      />
    )
  }

  return (
    <div className="min-h-screen bg-background p-4 md:p-6">
      <div className="max-w-6xl mx-auto">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-3xl font-bold flex items-center gap-2">
              <Target className="h-8 w-8" />
              목표 관리
            </h1>
            <p className="text-muted-foreground mt-2">목표를 설정하고 달성해보세요</p>
          </div>
          <Button
            onClick={() => {
              setEditingGoal(null)
              setIsFormOpen(true)
            }}
            className="flex items-center gap-2"
          >
            <Plus className="h-4 w-4" />새 목표 추가
          </Button>
        </div>

        {isFormOpen && (
          <div className="mb-8">
            <GoalForm
              goal={editingGoal}
              onSubmit={editingGoal ? (data) => updateGoal(editingGoal.goalId, data) : createGoal}
              onCancel={() => {
                setIsFormOpen(false)
                setEditingGoal(null)
              }}
            />
          </div>
        )}

        {loading ? (
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {Array.from({ length: 6 }).map((_, i) => (
              <Card key={i} className="animate-pulse">
                <CardHeader>
                  <div className="h-4 bg-muted rounded w-3/4"></div>
                  <div className="h-3 bg-muted rounded w-1/2"></div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-2">
                    <div className="h-3 bg-muted rounded"></div>
                    <div className="h-3 bg-muted rounded w-2/3"></div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        ) : goals.length === 0 ? (
          <Card className="text-center py-12">
            <CardContent>
              <Target className="h-12 w-12 mx-auto text-muted-foreground mb-4" />
              <h3 className="text-lg font-semibold mb-2">목표가 없습니다</h3>
              <p className="text-muted-foreground mb-4">첫 번째 목표를 추가해보세요</p>
              <Button onClick={() => setIsFormOpen(true)} className="flex items-center gap-2">
                <Plus className="h-4 w-4" />
                목표 추가하기
              </Button>
            </CardContent>
          </Card>
        ) : (
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {goals.map((goal) => (
              <Card
                key={goal.goalId}
                className={`cursor-pointer transition-all hover:shadow-md ${goal.completed ? "opacity-75" : ""}`}
                onClick={() => setSelectedGoal(goal)}
              >
                <CardHeader className="pb-3">
                  <div className="flex items-start justify-between">
                    <CardTitle className={`text-lg ${goal.completed ? "line-through" : ""}`}>{goal.title}</CardTitle>
                    <div className="flex gap-1">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={(e) => {
                          e.stopPropagation()
                          setEditingGoal(goal)
                          setIsFormOpen(true)
                        }}
                      >
                        <Edit className="h-4 w-4" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={(e) => {
                          e.stopPropagation()
                          deleteGoal(goal.goalId)
                        }}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge variant={getPriorityColor(goal.priority)}>{goal.priority}</Badge>
                    {goal.completed && (
                      <Badge variant="outline" className="text-green-600">
                        완료
                      </Badge>
                    )}
                  </div>
                </CardHeader>
                <CardContent className="space-y-3">
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <Calendar className="h-4 w-4" />
                    <span>
                      {formatDate(goal.startDate)} - {formatDate(goal.endDate)}
                    </span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-muted-foreground">
                    <Clock className="h-4 w-4" />
                    <span>{goal.duration}일</span>
                  </div>
                  {goal.subGoals.length > 0 && (
                    <div className="text-sm text-muted-foreground">
                      하위 목표: {goal.subGoals.filter((sg) => sg.completed).length}/{goal.subGoals.length}
                    </div>
                  )}
                  <Button
                    variant={goal.completed ? "secondary" : "default"}
                    size="sm"
                    className="w-full"
                    onClick={(e) => {
                      e.stopPropagation()
                      toggleGoalCompletion(goal.goalId)
                    }}
                  >
                    {goal.completed ? "완료됨" : "완료 표시"}
                  </Button>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
