"use client"

import { Menu, MenuButton, MenuItem, MenuItems } from "@headlessui/react"

export default function TestDropdown() {
    return (
        <div className="p-4">
            <Menu as="div" className="relative inline-block text-left">
                <MenuButton
                    as="button"
                    className="inline-flex justify-center rounded-md border border-border bg-primary px-4 py-2 text-sm font-medium text-primary-foreground hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-ring"
                    onClick={() => console.log("Test button clicked")} // Debug
                >
                    Mở Menu Test
                </MenuButton>
                <MenuItems
                    className="absolute right-0 mt-2 w-56 origin-top-right rounded-md bg-popover text-popover-foreground shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none z-[1000]"
                    anchor="bottom end"
                >
                    <MenuItem>
                        {({ active }) => (
                            <a
                                href="#"
                                className={`block px-4 py-2 text-sm ${active ? "bg-accent text-accent-foreground" : ""}`}
                            >
                                Test Item 1
                            </a>
                        )}
                    </MenuItem>
                    <MenuItem>
                        {({ active }) => (
                            <a
                                href="#"
                                className={`block px-4 py-2 text-sm ${active ? "bg-accent text-accent-foreground" : ""}`}
                            >
                                Test Item 2
                            </a>
                        )}
                    </MenuItem>
                    <MenuItem>
                        {({ active }) => (
                            <a
                                href="#"
                                className={`block px-4 py-2 text-sm ${active ? "bg-accent text-accent-foreground" : ""}`}
                            >
                                Test Item 3
                            </a>
                        )}
                    </MenuItem>
                </MenuItems>
            </Menu>
        </div>
    )
}