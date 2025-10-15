"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Checkbox } from "@/components/ui/checkbox"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Plus, Edit, Trash2 } from "lucide-react"

const sampleRoles = [
  {
    id: "1",
    name: "Admin",
    description: "Toàn quyền quản trị hệ thống",
    userCount: 2,
    permissions: ["user_management", "content_management", "system_settings", "reports_management"],
  },
  {
    id: "2",
    name: "Moderator",
    description: "Quản lý nội dung và người dùng",
    userCount: 5,
    permissions: ["content_management", "reports_management", "user_moderation"],
  },
  {
    id: "3",
    name: "User",
    description: "Người dùng thông thường",
    userCount: 2840,
    permissions: ["create_post", "comment", "like", "share"],
  },
]

const availablePermissions = [
  { id: "user_management", name: "Quản lý người dùng", description: "Tạo, sửa, xóa tài khoản người dùng" },
  { id: "content_management", name: "Quản lý nội dung", description: "Duyệt, xóa bài viết và bình luận" },
  { id: "system_settings", name: "Cài đặt hệ thống", description: "Thay đổi cấu hình hệ thống" },
  { id: "reports_management", name: "Quản lý báo cáo", description: "Xử lý báo cáo từ người dùng" },
  { id: "user_moderation", name: "Kiểm duyệt người dùng", description: "Khóa/mở khóa tài khoản" },
  { id: "create_post", name: "Tạo bài viết", description: "Đăng bài viết mới" },
  { id: "comment", name: "Bình luận", description: "Bình luận trên bài viết" },
  { id: "like", name: "Thích bài viết", description: "Thích và phản ứng bài viết" },
  { id: "share", name: "Chia sẻ", description: "Chia sẻ bài viết" },
]

export function RolesPermissions() {
  const [roles, setRoles] = useState(sampleRoles)
  const [selectedRole, setSelectedRole] = useState<any>(null)
  const [isDialogOpen, setIsDialogOpen] = useState(false)

  const handlePermissionToggle = (roleId: string, permissionId: string) => {
    setRoles(
      roles.map((role) => {
        if (role.id === roleId) {
          const hasPermission = role.permissions.includes(permissionId)
          return {
            ...role,
            permissions: hasPermission
              ? role.permissions.filter((p) => p !== permissionId)
              : [...role.permissions, permissionId],
          }
        }
        return role
      }),
    )
  }

  return (
    <div className="space-y-6">
      {/* Roles Table */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle>Danh sách vai trò</CardTitle>
            <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
              <DialogTrigger asChild>
                <Button>
                  <Plus className="h-4 w-4 mr-2" />
                  Thêm vai trò
                </Button>
              </DialogTrigger>
              <DialogContent>
                <DialogHeader>
                  <DialogTitle>Tạo vai trò mới</DialogTitle>
                  <DialogDescription>Tạo vai trò mới và gán quyền hạn tương ứng</DialogDescription>
                </DialogHeader>
                <div className="space-y-4">
                  <div>
                    <label className="text-sm font-medium">Tên vai trò</label>
                    <Input placeholder="Nhập tên vai trò" />
                  </div>
                  <div>
                    <label className="text-sm font-medium">Mô tả</label>
                    <Input placeholder="Mô tả vai trò" />
                  </div>
                  <div className="flex justify-end space-x-2">
                    <Button variant="outline" onClick={() => setIsDialogOpen(false)}>
                      Hủy
                    </Button>
                    <Button onClick={() => setIsDialogOpen(false)}>Tạo vai trò</Button>
                  </div>
                </div>
              </DialogContent>
            </Dialog>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Vai trò</TableHead>
                <TableHead>Mô tả</TableHead>
                <TableHead>Số người dùng</TableHead>
                <TableHead>Quyền hạn</TableHead>
                <TableHead className="w-[100px]">Thao tác</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {roles.map((role) => (
                <TableRow key={role.id}>
                  <TableCell>
                    <div>
                      <p className="font-medium">{role.name}</p>
                    </div>
                  </TableCell>
                  <TableCell>{role.description}</TableCell>
                  <TableCell>
                    <Badge variant="outline">{role.userCount} người</Badge>
                  </TableCell>
                  <TableCell>
                    <div className="flex flex-wrap gap-1">
                      {role.permissions.slice(0, 3).map((permission) => (
                        <Badge key={permission} variant="secondary" className="text-xs">
                          {availablePermissions.find((p) => p.id === permission)?.name}
                        </Badge>
                      ))}
                      {role.permissions.length > 3 && (
                        <Badge variant="outline" className="text-xs">
                          +{role.permissions.length - 3}
                        </Badge>
                      )}
                    </div>
                  </TableCell>
                  <TableCell>
                    <div className="flex space-x-2">
                      <Button variant="ghost" size="sm" onClick={() => setSelectedRole(role)}>
                        <Edit className="h-4 w-4" />
                      </Button>
                      <Button variant="ghost" size="sm">
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* Permissions Management */}
      {selectedRole && (
        <Card>
          <CardHeader>
            <CardTitle>Quản lý quyền hạn - {selectedRole.name}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid gap-4 md:grid-cols-2">
              {availablePermissions.map((permission) => (
                <div key={permission.id} className="flex items-start space-x-3 p-3 border rounded-lg">
                  <Checkbox
                    id={permission.id}
                    checked={selectedRole.permissions.includes(permission.id)}
                    onCheckedChange={() => handlePermissionToggle(selectedRole.id, permission.id)}
                  />
                  <div className="flex-1">
                    <label htmlFor={permission.id} className="text-sm font-medium cursor-pointer">
                      {permission.name}
                    </label>
                    <p className="text-xs text-muted-foreground mt-1">{permission.description}</p>
                  </div>
                </div>
              ))}
            </div>
            <div className="flex justify-end mt-6">
              <Button onClick={() => setSelectedRole(null)}>Lưu thay đổi</Button>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
