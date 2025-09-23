import { AdminSidebar } from "@/components/admin/admin-sidebar"
import { AdminNavbar } from "@/components/admin/admin-navbar"
import { StatsCard } from "@/components/admin/stats-card"
import { RecentActivity } from "@/components/admin/recent-activity"
import { Users, FileText, AlertTriangle, TrendingUp } from "lucide-react"

export default function AdminDashboard() {
  return (
    <div className="flex h-screen bg-background">
      <AdminSidebar />

      <div className="flex-1 flex flex-col overflow-hidden">
        <AdminNavbar />

        <main className="flex-1 overflow-y-auto p-6">
          <div className="space-y-6">
            {/* Page Header */}
            <div>
              <h1 className="text-3xl font-bold">Dashboard</h1>
              <p className="text-muted-foreground">Tổng quan về hoạt động của hệ thống</p>
            </div>

            {/* Stats Grid */}
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
              <StatsCard
                title="Tổng số người dùng"
                value="2,847"
                change="+12% so với tháng trước"
                changeType="positive"
                icon={Users}
              />
              <StatsCard
                title="Bài viết"
                value="1,234"
                change="+8% so với tháng trước"
                changeType="positive"
                icon={FileText}
              />
              <StatsCard
                title="Báo cáo chờ xử lý"
                value="23"
                change="+3 báo cáo mới"
                changeType="negative"
                icon={AlertTriangle}
              />
              <StatsCard
                title="Tăng trưởng"
                value="15.2%"
                change="Tăng trưởng người dùng"
                changeType="positive"
                icon={TrendingUp}
              />
            </div>

            {/* Content Grid */}
            <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
              <div className="lg:col-span-2">
                <RecentActivity />
              </div>

              <div className="space-y-6">
                {/* Quick Actions */}
                <div className="bg-card rounded-lg p-6 border">
                  <h3 className="text-lg font-semibold mb-4">Thao tác nhanh</h3>
                  <div className="space-y-2">
                    <button className="w-full text-left p-2 rounded hover:bg-accent transition-colors">
                      Xem báo cáo mới
                    </button>
                    <button className="w-full text-left p-2 rounded hover:bg-accent transition-colors">
                      Quản lý người dùng
                    </button>
                    <button className="w-full text-left p-2 rounded hover:bg-accent transition-colors">
                      Cài đặt hệ thống
                    </button>
                  </div>
                </div>

                {/* System Status */}
                <div className="bg-card rounded-lg p-6 border">
                  <h3 className="text-lg font-semibold mb-4">Trạng thái hệ thống</h3>
                  <div className="space-y-3">
                    <div className="flex items-center justify-between">
                      <span className="text-sm">Server</span>
                      <div className="flex items-center space-x-2">
                        <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                        <span className="text-sm text-green-600">Hoạt động</span>
                      </div>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-sm">Database</span>
                      <div className="flex items-center space-x-2">
                        <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                        <span className="text-sm text-green-600">Hoạt động</span>
                      </div>
                    </div>
                    <div className="flex items-center justify-between">
                      <span className="text-sm">Storage</span>
                      <div className="flex items-center space-x-2">
                        <div className="w-2 h-2 bg-yellow-500 rounded-full"></div>
                        <span className="text-sm text-yellow-600">Cảnh báo</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </main>
      </div>
    </div>
  )
}
