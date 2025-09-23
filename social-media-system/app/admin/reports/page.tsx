import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminNavbar } from "@/components/admin/admin-navbar"
import { ReportsTable } from "@/components/admin/reports-table"

export default function ReportsManagementPage() {
  return (
    <div className="flex h-screen bg-background">
      <AdminSidebar />

      <div className="flex-1 flex flex-col overflow-hidden">
        <AdminNavbar />

        <main className="flex-1 overflow-y-auto p-6">
          <div className="space-y-6">
            <div>
              <h1 className="text-3xl font-bold">Quản lý báo cáo</h1>
              <p className="text-muted-foreground">Xem xét và xử lý các báo cáo từ người dùng</p>
            </div>

            <ReportsTable />
          </div>
        </main>
      </div>
    </div>
  )
}
