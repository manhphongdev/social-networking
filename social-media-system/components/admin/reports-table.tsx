"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Search, MoreHorizontal, Check, X, Eye } from "lucide-react"

const sampleReports = [
  {
    id: "1",
    reporter: {
      name: "Nguyễn Văn A",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    reportedUser: {
      name: "Trần Thị B",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    postContent: "Nội dung bài viết không phù hợp với cộng đồng...",
    reason: "Spam",
    status: "pending",
    createdAt: "2024-01-20 10:30",
    description: "Bài viết chứa nội dung spam và quảng cáo không phù hợp",
  },
  {
    id: "2",
    reporter: {
      name: "Lê Văn C",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    reportedUser: {
      name: "Phạm Thị D",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    postContent: "Bình luận có nội dung xúc phạm...",
    reason: "Harassment",
    status: "approved",
    createdAt: "2024-01-19 15:45",
    description: "Bình luận có tính chất quấy rối và xúc phạm người khác",
  },
  {
    id: "3",
    reporter: {
      name: "Hoàng Văn E",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    reportedUser: {
      name: "Ngô Thị F",
      avatar: "/placeholder.svg?height=32&width=32",
    },
    postContent: "Chia sẻ thông tin sai lệch về sức khỏe...",
    reason: "Misinformation",
    status: "rejected",
    createdAt: "2024-01-18 09:15",
    description: "Thông tin y tế không chính xác có thể gây hại",
  },
]

export function ReportsTable() {
  const [searchQuery, setSearchQuery] = useState("")
  const [reports, setReports] = useState(sampleReports)

  const filteredReports = reports.filter(
    (report) =>
      report.reporter.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      report.reportedUser.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      report.reason.toLowerCase().includes(searchQuery.toLowerCase()),
  )

  const handleStatusChange = (reportId: string, newStatus: string) => {
    setReports(reports.map((report) => (report.id === reportId ? { ...report, status: newStatus } : report)))
  }

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "pending":
        return <Badge variant="secondary">Chờ xử lý</Badge>
      case "approved":
        return <Badge variant="default">Đã duyệt</Badge>
      case "rejected":
        return <Badge variant="destructive">Từ chối</Badge>
      case "resolved":
        return <Badge variant="outline">Đã giải quyết</Badge>
      default:
        return <Badge variant="outline">Không xác định</Badge>
    }
  }

  const getReasonBadge = (reason: string) => {
    switch (reason) {
      case "Spam":
        return <Badge variant="destructive">Spam</Badge>
      case "Harassment":
        return <Badge variant="destructive">Quấy rối</Badge>
      case "Misinformation":
        return <Badge variant="secondary">Thông tin sai</Badge>
      case "Inappropriate":
        return <Badge variant="outline">Không phù hợp</Badge>
      default:
        return <Badge variant="outline">{reason}</Badge>
    }
  }

  return (
    <div className="space-y-4">
      {/* Search */}
      <div className="flex items-center space-x-2">
        <div className="relative flex-1 max-w-sm">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Tìm kiếm báo cáo..."
            className="pl-10"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
      </div>

      {/* Table */}
      <div className="border rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Người báo cáo</TableHead>
              <TableHead>Người bị báo cáo</TableHead>
              <TableHead>Lý do</TableHead>
              <TableHead>Nội dung</TableHead>
              <TableHead>Trạng thái</TableHead>
              <TableHead>Thời gian</TableHead>
              <TableHead className="w-[100px]">Thao tác</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredReports.map((report) => (
              <TableRow key={report.id}>
                <TableCell>
                  <div className="flex items-center space-x-3">
                    <Avatar className="w-8 h-8">
                      <AvatarImage src={report.reporter.avatar || "/placeholder.svg"} />
                      <AvatarFallback>{report.reporter.name[0]}</AvatarFallback>
                    </Avatar>
                    <span className="font-medium">{report.reporter.name}</span>
                  </div>
                </TableCell>
                <TableCell>
                  <div className="flex items-center space-x-3">
                    <Avatar className="w-8 h-8">
                      <AvatarImage src={report.reportedUser.avatar || "/placeholder.svg"} />
                      <AvatarFallback>{report.reportedUser.name[0]}</AvatarFallback>
                    </Avatar>
                    <span className="font-medium">{report.reportedUser.name}</span>
                  </div>
                </TableCell>
                <TableCell>{getReasonBadge(report.reason)}</TableCell>
                <TableCell>
                  <div className="max-w-xs">
                    <p className="text-sm truncate">{report.postContent}</p>
                    <p className="text-xs text-muted-foreground mt-1">{report.description}</p>
                  </div>
                </TableCell>
                <TableCell>{getStatusBadge(report.status)}</TableCell>
                <TableCell>{report.createdAt}</TableCell>
                <TableCell>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" size="sm">
                        <MoreHorizontal className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <DropdownMenuItem>
                        <Eye className="mr-2 h-4 w-4" />
                        Xem chi tiết
                      </DropdownMenuItem>
                      {report.status === "pending" && (
                        <>
                          <DropdownMenuItem onClick={() => handleStatusChange(report.id, "approved")}>
                            <Check className="mr-2 h-4 w-4" />
                            Duyệt
                          </DropdownMenuItem>
                          <DropdownMenuItem onClick={() => handleStatusChange(report.id, "rejected")}>
                            <X className="mr-2 h-4 w-4" />
                            Từ chối
                          </DropdownMenuItem>
                        </>
                      )}
                      <DropdownMenuItem onClick={() => handleStatusChange(report.id, "resolved")}>
                        <Check className="mr-2 h-4 w-4" />
                        Đánh dấu đã giải quyết
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}
