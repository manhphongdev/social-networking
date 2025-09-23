import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminNavbar } from "@/components/admin/admin-navbar"
import { RolesPermissions } from "@/components/admin/roles-permissions"

export default function RolesManagementPage() {
  return (
    <div className="flex h-screen bg-background">
      <AdminSidebar />

      <div className="flex-1 flex flex-col overflow-hidden">
        <AdminNavbar />

        <main className="flex-1 overflow-y-auto p-6">
          <div className="space-y-6">
            <div>
              <h1 className="text-3xl font-bold">Roles & Permissions</h1>
              <p className="text-muted-foreground">Quản lý vai trò và phân quyền trong hệ thống</p>
            </div>

            <RolesPermissions />
          </div>
        </main>
      </div>
    </div>
  )
}
