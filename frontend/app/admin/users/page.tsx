import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminNavbar } from "@/components/admin/admin-navbar"
import { UserManagementTable } from "@/components/admin/user-management-table"

export default function UsersManagementPage() {
  return (
    <div className="flex h-screen bg-background">
      <AdminSidebar />

      <div className="flex-1 flex flex-col overflow-hidden">
        <AdminNavbar />

        <main className="flex-1 overflow-y-auto p-6">
          <div className="space-y-6">
            <div>
              <h1 className="text-3xl font-bold">Quản lý người dùng</h1>
              <p className="text-muted-foreground">Quản lý tài khoản và quyền hạn của người dùng</p>
            </div>

            <UserManagementTable />
          </div>
        </main>
      </div>
    </div>
  )
}
