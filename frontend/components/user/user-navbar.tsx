"use client"

import {useState} from "react"
import Link from "next/link"
import {Button} from "@/components/ui/button"
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {Input} from "@/components/ui/input"
import {Bell, Home, LogOut, Menu as HamburgerMenu, MessageCircle, Search, Settings, User} from "lucide-react"
import {Menu} from "@headlessui/react"
import {useAuth} from "@/app/context/AuthContext"

export function UserNavbar() {
    const [isMenuOpen, setIsMenuOpen] = useState(false)
    const {isAuthenticated, avatarUrl, logout} = useAuth()

    return (
        <nav
            className="sticky top-0 z-50 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 border-b">
            <div className="container mx-auto px-4">
                <div className="flex items-center justify-between h-16">
                    {/* Logo */}
                    <Link href="/feed" className="flex items-center space-x-2">
                        <div className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
                            <span className="text-primary-foreground font-bold text-sm">SN</span>
                        </div>
                        <span className="font-bold text-xl hidden sm:block">SocialNet</span>
                    </Link>

                    {/* Search Desktop */}
                    <div className="hidden md:flex flex-1 max-w-md mx-8">
                        <div className="relative w-full">
                            <Search
                                className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground"/>
                            <Input placeholder="Tìm kiếm bạn bè, bài viết..." className="pl-10 w-full"/>
                        </div>
                    </div>

                    {/* Right Side */}
                    <div className="flex items-center space-x-2">
                        {/* Desktop icons */}
                        <div className="hidden md:flex items-center space-x-1">
                            <Button variant="ghost" size="sm" asChild>
                                <Link href="/feed"><Home className="h-5 w-5"/></Link>
                            </Button>
                            <Button variant="ghost" size="sm" asChild>
                                <Link href="/notifications"><Bell className="h-5 w-5"/></Link>
                            </Button>
                            <Button variant="ghost" size="sm" asChild>
                                <Link href="/chat"><MessageCircle className="h-5 w-5"/></Link>
                            </Button>
                        </div>

                        {/* User menu or login/register */}
                        {isAuthenticated ? (
                            <div className="hidden md:block">
                                <Menu as="div" className="relative">
                                    <Menu.Button as={Button} variant="ghost" className="h-8 w-8 rounded-full">
                                        <Avatar className="h-8 w-8">
                                            <AvatarImage src={avatarUrl || "/placeholder.svg"} alt="Avatar"/>
                                            <AvatarFallback>U</AvatarFallback>
                                        </Avatar>
                                    </Menu.Button>
                                    <Menu.Items
                                        className="absolute right-0 mt-2 w-56 origin-top-right bg-background border rounded-md shadow-lg focus:outline-none">
                                        <Menu.Item>
                                            {({active}) => (
                                                <Link href="/profile"
                                                      className={`flex items-center px-4 py-2 text-sm ${active ? "bg-accent" : ""}`}>
                                                    <User className="mr-2 h-4 w-4"/> Hồ sơ cá nhân
                                                </Link>
                                            )}
                                        </Menu.Item>
                                        <Menu.Item>
                                            {({active}) => (
                                                <Link href="/settings"
                                                      className={`flex items-center px-4 py-2 text-sm ${active ? "bg-accent" : ""}`}>
                                                    <Settings className="mr-2 h-4 w-4"/> Cài đặt
                                                </Link>
                                            )}
                                        </Menu.Item>
                                        <div className="border-t my-1"/>
                                        <Menu.Item>
                                            {({active}) => (
                                                <button
                                                    onClick={logout}
                                                    className={`flex items-center w-full px-4 py-2 text-sm text-left ${active ? "bg-accent" : ""}`}
                                                >
                                                    <LogOut className="mr-2 h-4 w-4"/> Đăng xuất
                                                </button>
                                            )}
                                        </Menu.Item>
                                    </Menu.Items>
                                </Menu>
                            </div>
                        ) : (
                            <div className="flex items-center space-x-2">
                                <Button variant="outline" size="sm" asChild>
                                    <Link href="/auth/login">Đăng nhập</Link>
                                </Button>
                                <Button size="sm" asChild>
                                    <Link href="/auth/register">Đăng ký</Link>
                                </Button>
                            </div>
                        )}

                        {/* Mobile menu button */}
                        <Button variant="ghost" size="sm" className="md:hidden"
                                onClick={() => setIsMenuOpen(!isMenuOpen)}>
                            <HamburgerMenu className="h-5 w-5"/>
                        </Button>
                    </div>
                </div>

                {/* Mobile navigation */}
                {isMenuOpen && (
                    <div className="md:hidden pb-4 space-y-2">
                        <Button variant="ghost" className="w-full justify-start" asChild>
                            <Link href="/feed"><Home className="mr-2 h-4 w-4"/> Trang chủ</Link>
                        </Button>
                        <Button variant="ghost" className="w-full justify-start" asChild>
                            <Link href="/notifications"><Bell className="mr-2 h-4 w-4"/> Thông báo</Link>
                        </Button>
                        <Button variant="ghost" className="w-full justify-start" asChild>
                            <Link href="/chat"><MessageCircle className="mr-2 h-4 w-4"/> Tin nhắn</Link>
                        </Button>
                        {isAuthenticated ? (
                            <>
                                <Button variant="ghost" className="w-full justify-start" asChild>
                                    <Link href="/profile"><User className="mr-2 h-4 w-4"/> Hồ sơ cá nhân</Link>
                                </Button>
                                <Button variant="ghost" className="w-full justify-start" asChild>
                                    <Link href="/settings"><Settings className="mr-2 h-4 w-4"/> Cài đặt</Link>
                                </Button>
                                <Button variant="ghost" className="w-full justify-start" onClick={logout}>
                                    <LogOut className="mr-2 h-4 w-4"/> Đăng xuất
                                </Button>
                            </>
                        ) : (
                            <>
                                <Button variant="ghost" className="w-full justify-start" asChild>
                                    <Link href="/auth/login">Đăng nhập</Link>
                                </Button>
                                <Button variant="ghost" className="w-full justify-start" asChild>
                                    <Link href="/auth/register">Đăng ký</Link>
                                </Button>
                            </>
                        )}
                    </div>
                )}
            </div>
        </nav>
    )
}
