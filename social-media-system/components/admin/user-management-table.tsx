"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Search, MoreHorizontal, Lock, Unlock, UserX, Edit } from "lucide-react"

const sampleUsers = [
  {
    id: "1",
    email: "nguyen.van.a@email.com",
    name: "Nguyễn Văn A",
    avatar: "/placeholder.svg?height=32&width=32",
    status: "active",
    role: "user",
    joinDate: "2024-01-15",
    lastActive: "2024-01-20",
  },
  {
    id: "2",
    email: "tran.thi.b@email.com",
    name: "Trần Thị B",
    avatar: "/placeholder.svg?height=32&width=32",
    status: "active",
    role: "moderator",
    joinDate: "2024-01-10",
    lastActive: "2024-01-19",
  },
  {
    id: "3",
    email: "le.van.c@email.com",
    name: "Lê Văn C",
    avatar: "/placeholder.svg?height=32&width=32",
    status: "suspended",
    role: "user",
    joinDate: "2024-01-05",
    lastActive: "2024-01-18",
  },
  {
    id: "4",
    email: "pham.thi.d@email.com",
    name: "Phạm Thị D",
    avatar: "/placeholder.svg?height=32&width=32",
    status: "active",
    role: "user",
    joinDate: "2024-01-12",
    lastActive: "2024-01-20",
  },
]

export function UserManagementTable() {
  const [searchQuery, setSearchQuery] = useState("")
  const [users, setUsers] = useState(sampleUsers)

  const filteredUsers = users.filter(
    (user) =>
      user.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.toLowerCase()),
  )

  const handleStatusToggle = (userId: string) => {
    setUsers(
      users.map((user) =>
        user.id === userId ? { ...user, status: user.status === "active" ? "suspended" : "active" } : user,
      ),
    )
  }

  const getStatusBadge = (status: string) => {
    switch (status) {
      case "active":
        return <Badge variant="default">Hoạt động</Badge>
      case "suspended":
        return <Badge variant="destructive">Tạm khóa</Badge>
      case "banned":
        return <Badge variant="secondary">Cấm vĩnh viễn</Badge>
      default:
        return <Badge variant="outline">Không xác định</Badge>
    }
  }

  const getRoleBadge = (role: string) => {
    switch (role) {
      case "admin":
        return <Badge variant="default">Admin</Badge>
      case "moderator":
        return <Badge variant="secondary">Moderator</Badge>
      case "user":
        return <Badge variant="outline">User</Badge>
      default:
        return <Badge variant="outline">Không xác định</Badge>
    }
  }

  return (
    <div className="space-y-4">
      {/* Search */}
      <div className="flex items-center space-x-2">
        <div className="relative flex-1 max-w-sm">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Tìm kiếm người dùng..."
            className="pl-10"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
          />
        </div>
        <Button>Thêm người dùng</Button>
      </div>

      {/* Table */}
      <div className="border rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Người dùng</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>Trạng thái</TableHead>
              <TableHead>Vai trò</TableHead>
              <TableHead>Ngày tham gia</TableHead>
              <TableHead>Hoạt động cuối</TableHead>
              <TableHead className="w-[100px]">Thao tác</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {filteredUsers.map((user) => (
              <TableRow key={user.id}>
                <TableCell>
                  <div className="flex items-center space-x-3">
                    <Avatar className="w-8 h-8">
                      <AvatarImage src={user.avatar || "/placeholder.svg"} />
                      <AvatarFallback>{user.name[0]}</AvatarFallback>
                    </Avatar>
                    <span className="font-medium">{user.name}</span>
                  </div>
                </TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{getStatusBadge(user.status)}</TableCell>
                <TableCell>{getRoleBadge(user.role)}</TableCell>
                <TableCell>{user.joinDate}</TableCell>
                <TableCell>{user.lastActive}</TableCell>
                <TableCell>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" size="sm">
                        <MoreHorizontal className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <DropdownMenuItem>
                        <Edit className="mr-2 h-4 w-4" />
                        Chỉnh sửa
                      </DropdownMenuItem>
                      <DropdownMenuItem onClick={() => handleStatusToggle(user.id)}>
                        {user.status === "active" ? (
                          <>
                            <Lock className="mr-2 h-4 w-4" />
                            Tạm khóa
                          </>
                        ) : (
                          <>
                            <Unlock className="mr-2 h-4 w-4" />
                            Mở khóa
                          </>
                        )}
                      </DropdownMenuItem>
                      <DropdownMenuItem className="text-destructive">
                        <UserX className="mr-2 h-4 w-4" />
                        Xóa tài khoản
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
