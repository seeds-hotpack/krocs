"use client"

import type React from "react"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { X } from "lucide-react"

interface Goal {
  goalId: number
  title: string
  priority: "LOW" | "MEDIUM" | "HIGH"
  startDate: string
  endDate: string
  duration: number
  completed: boolean
  subGoals: any[]
  createdAt: string
  updatedAt: string
}

interface GoalFormProps {
  goal?: Goal | null
  onSubmit: (data: any) => void
  onCancel: () => void
}

export function GoalForm({ goal, onSubmit, onCancel }: GoalFormProps) {
  const [formData, setFormData] = useState({
    title: goal?.title || "",
    priority: goal?.priority || "MEDIUM",
    startDate: goal?.startDate || new Date().toISOString().split("T")[0],
    endDate: goal?.endDate || new Date().toISOString().split("T")[0],
    duration: goal?.duration || 0,
  })

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()

    // Calculate duration if not provided
    const start = new Date(formData.startDate)
    const end = new Date(formData.endDate)
    const calculatedDuration = Math.ceil((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))

    onSubmit({
      ...formData,
      duration: calculatedDuration,
    })
  }

  const handleDateChange = (field: "startDate" | "endDate", value: string) => {
    const newFormData = { ...formData, [field]: value }

    // Auto-calculate duration when dates change
    if (newFormData.startDate && newFormData.endDate) {
      const start = new Date(newFormData.startDate)
      const end = new Date(newFormData.endDate)
      const duration = Math.ceil((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24))
      newFormData.duration = Math.max(0, duration)
    }

    setFormData(newFormData)
  }

  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between">
        <CardTitle>{goal ? "목표 수정" : "새 목표 추가"}</CardTitle>
        <Button variant="ghost" size="sm" onClick={onCancel}>
          <X className="h-4 w-4" />
        </Button>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="title">목표 제목</Label>
            <Input
              id="title"
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
              placeholder="목표를 입력하세요"
              required
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="priority">우선순위</Label>
            <Select
              value={formData.priority}
              onValueChange={(value: "LOW" | "MEDIUM" | "HIGH") => setFormData({ ...formData, priority: value })}
            >
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="LOW">낮음</SelectItem>
                <SelectItem value="MEDIUM">보통</SelectItem>
                <SelectItem value="HIGH">높음</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label htmlFor="startDate">시작일</Label>
              <Input
                id="startDate"
                type="date"
                value={formData.startDate}
                onChange={(e) => handleDateChange("startDate", e.target.value)}
                required
              />
            </div>

            <div className="space-y-2">
              <Label htmlFor="endDate">종료일</Label>
              <Input
                id="endDate"
                type="date"
                value={formData.endDate}
                onChange={(e) => handleDateChange("endDate", e.target.value)}
                min={formData.startDate}
                required
              />
            </div>
          </div>

          <div className="space-y-2">
            <Label htmlFor="duration">기간 (일)</Label>
            <Input
              id="duration"
              type="number"
              value={formData.duration}
              onChange={(e) => setFormData({ ...formData, duration: Number.parseInt(e.target.value) || 0 })}
              min="0"
              readOnly
              className="bg-muted"
            />
            <p className="text-sm text-muted-foreground">시작일과 종료일을 기준으로 자동 계산됩니다</p>
          </div>

          <div className="flex gap-2 pt-4">
            <Button type="submit" className="flex-1">
              {goal ? "수정하기" : "추가하기"}
            </Button>
            <Button type="button" variant="outline" onClick={onCancel}>
              취소
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  )
}
