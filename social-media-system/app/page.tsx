import Link from "next/link"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Users, Shield, MessageCircle, TrendingUp, Home, Bell, User, Settings } from "lucide-react"

export default function HomePage() {
  return (
    <div className="min-h-screen bg-background">
      {/* Hero Section */}
      <div className="relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-background to-primary/5" />
        <div className="relative container mx-auto px-4 py-20">
          <div className="text-center space-y-8">
            <div className="space-y-4">
              <h1 className="text-4xl md:text-6xl font-bold text-balance">
                Chào mừng đến với <span className="text-primary">SocialNet</span>
              </h1>
              <p className="text-xl md:text-2xl text-muted-foreground text-balance max-w-3xl mx-auto">
                Kết nối, chia sẻ và khám phá cùng cộng đồng của bạn. Nền tảng mạng xã hội hiện đại với trải nghiệm tuyệt
                vời.
              </p>
            </div>

            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button size="lg" asChild>
                <Link href="/auth/register">Tham gia ngay</Link>
              </Button>
              <Button variant="outline" size="lg" asChild>
                <Link href="/auth/login">Đăng nhập</Link>
              </Button>
            </div>
          </div>
        </div>
      </div>

      {/* Quick Navigation Section */}
      <div className="container mx-auto px-4 py-12 border-b">
        <div className="text-center mb-8">
          <h2 className="text-2xl font-bold mb-4">Liên kết nhanh để test</h2>
          <p className="text-muted-foreground">Các trang chính của hệ thống</p>
        </div>

        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3 max-w-4xl mx-auto">
          <Card className="hover:shadow-md transition-shadow">
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-lg">
                <Home className="h-5 w-5 text-primary" />
                Trang người dùng
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              <Button variant="outline" size="sm" className="w-full justify-start bg-transparent" asChild>
                <Link href="/feed">
                  <Home className="h-4 w-4 mr-2" />
                  News Feed
                </Link>
              </Button>
              <Button variant="outline" size="sm" className="w-full justify-start bg-transparent" asChild>
                <Link href="/profile">
                  <User className="h-4 w-4 mr-2" />
                  Hồ sơ cá nhân
                </Link>
              </Button>
              <Button variant="outline" size="sm" className="w-full justify-start bg-transparent" asChild>
                <Link href="/notifications">
                  <Bell className="h-4 w-4 mr-2" />
                  Thông báo
                </Link>
              </Button>
              <Button variant="outline" size="sm" className="w-full justify-start bg-transparent" asChild>
                <Link href="/chat">
                  <MessageCircle className="h-4 w-4 mr-2" />
                  Tin nhắn
                </Link>
              </Button>
            </CardContent>
          </Card>

          <Card className="hover:shadow-md transition-shadow">
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-lg">
                <Shield className="h-5 w-5 text-primary" />
                Trang quản trị
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              <Button variant="outline" size="sm" className="w-full justify-start bg-transparent" asChild>
                <Link href="/admin">
                  <TrendingUp className="h-4 w-4 mr-2" />
                  Dashboard
                </Link>
              </Button>
              <Button variant="outline" size="sm" className="w-full justify-start bg-transparent" asChild>
                <Link href="/admin/users">
                  <Users className="h-4 w-4 mr-2" />
                  Quản lý người dùng
                </Link>
              </Button>
              <Button variant="outline" size="sm" className="w-full justify-start bg-transparent" asChild>
                <Link href="/admin/reports">
                  <Shield className="h-4 w-4 mr-2" />
                  Quản lý báo cáo
                </Link>
              </Button>
              <Button variant="outline" size="sm" className="w-full justify-start bg-transparent" asChild>
                <Link href="/admin/roles">
                  <Settings className="h-4 w-4 mr-2" />
                  Roles & Permissions
                </Link>
              </Button>
            </CardContent>
          </Card>

          <Card className="hover:shadow-md transition-shadow">
            <CardHeader className="pb-3">
              <CardTitle className="flex items-center gap-2 text-lg">
                <User className="h-5 w-5 text-primary" />
                Tài khoản test
              </CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              <div className="text-sm text-muted-foreground mb-2">Người dùng:</div>
              <div className="text-sm font-mono bg-muted p-2 rounded">
                user@test.com
                <br />
                123456
              </div>
              <div className="text-sm text-muted-foreground mb-2 mt-3">Quản trị viên:</div>
              <div className="text-sm font-mono bg-muted p-2 rounded">
                admin@test.com
                <br />
                admin123
              </div>
            </CardContent>
          </Card>
        </div>
      </div>

      {/* Features Section */}
      <div className="container mx-auto px-4 py-20">
        <div className="text-center mb-16">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">Tính năng nổi bật</h2>
          <p className="text-xl text-muted-foreground text-balance max-w-2xl mx-auto">
            Khám phá những tính năng tuyệt vời giúp bạn kết nối và chia sẻ một cách hiệu quả
          </p>
        </div>

        <div className="grid gap-8 md:grid-cols-2 lg:grid-cols-4">
          <Card className="text-center">
            <CardHeader>
              <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                <Users className="h-6 w-6 text-primary" />
              </div>
              <CardTitle>Kết nối bạn bè</CardTitle>
            </CardHeader>
            <CardContent>
              <CardDescription>
                Tìm kiếm và kết nối với bạn bè, đồng nghiệp và những người có cùng sở thích
              </CardDescription>
            </CardContent>
          </Card>

          <Card className="text-center">
            <CardHeader>
              <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                <MessageCircle className="h-6 w-6 text-primary" />
              </div>
              <CardTitle>Trò chuyện thời gian thực</CardTitle>
            </CardHeader>
            <CardContent>
              <CardDescription>
                Nhắn tin trực tiếp với bạn bè, chia sẻ hình ảnh và cảm xúc một cách nhanh chóng
              </CardDescription>
            </CardContent>
          </Card>

          <Card className="text-center">
            <CardHeader>
              <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                <TrendingUp className="h-6 w-6 text-primary" />
              </div>
              <CardTitle>Nội dung thịnh hành</CardTitle>
            </CardHeader>
            <CardContent>
              <CardDescription>
                Khám phá những bài viết hot nhất, xu hướng mới và nội dung được yêu thích
              </CardDescription>
            </CardContent>
          </Card>

          <Card className="text-center">
            <CardHeader>
              <div className="w-12 h-12 bg-primary/10 rounded-lg flex items-center justify-center mx-auto mb-4">
                <Shield className="h-6 w-6 text-primary" />
              </div>
              <CardTitle>An toàn & Bảo mật</CardTitle>
            </CardHeader>
            <CardContent>
              <CardDescription>
                Hệ thống bảo mật cao cấp, kiểm duyệt nội dung và bảo vệ quyền riêng tư người dùng
              </CardDescription>
            </CardContent>
          </Card>
        </div>
      </div>

      {/* CTA Section */}
      <div className="bg-primary/5 border-t">
        <div className="container mx-auto px-4 py-20">
          <div className="text-center space-y-8">
            <div className="space-y-4">
              <h2 className="text-3xl md:text-4xl font-bold">Sẵn sàng tham gia cộng đồng?</h2>
              <p className="text-xl text-muted-foreground text-balance max-w-2xl mx-auto">
                Hàng nghìn người dùng đã tin tưởng và sử dụng SocialNet để kết nối và chia sẻ
              </p>
            </div>

            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <Button size="lg" asChild>
                <Link href="/auth/register">Đăng ký miễn phí</Link>
              </Button>
              <Button variant="outline" size="lg" asChild>
                <Link href="/admin">Quản trị viên</Link>
              </Button>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="border-t bg-background">
        <div className="container mx-auto px-4 py-8">
          <div className="flex flex-col md:flex-row items-center justify-between">
            <div className="flex items-center space-x-2 mb-4 md:mb-0">
              <div className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
                <span className="text-primary-foreground font-bold text-sm">SN</span>
              </div>
              <span className="font-bold text-xl">SocialNet</span>
            </div>
            <p className="text-sm text-muted-foreground">© 2024 SocialNet. Tất cả quyền được bảo lưu.</p>
          </div>
        </div>
      </footer>
    </div>
  )
}
