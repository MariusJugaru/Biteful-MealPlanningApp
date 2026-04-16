import { UtensilsCrossed, Home, CookingPot, LucideScrollText, Bookmark, User2 } from "lucide-react";
import NavButton from "./NavButton";

function Header() {
    return(
        <header className="border-b sticky top-0 bg-[#ffffff] z-10">
            <div className="mx-auto px-6 py-4">
                <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                        <NavButton to="/">
                            <div className="bg-black text-white p-2 rounded-lg">
                                <UtensilsCrossed className="w-6 h-6" />
                            </div>
                            <h2>Biteful</h2>
                        </NavButton>
                    </div>
                    <nav className="flex items-center gap-2">
                        <NavButton to="/">
                            <Home className="w-5 h-5"></Home>
                            Home
                        </NavButton>
                        <NavButton to="/recipes">
                            <CookingPot className="w-5 h-5"></CookingPot>
                            Recipes
                        </NavButton>
                        <NavButton to="/saved">
                            <Bookmark className="w-5 h-5"></Bookmark>
                            Saved
                        </NavButton>
                        <NavButton to="/lists">
                            <LucideScrollText className="w-5 h-5"></LucideScrollText>
                            Lists
                        </NavButton>
                        <NavButton to="/settings">
                            <User2 className="w-5 h-5"></User2>
                            Profile
                        </NavButton>
                    </nav>
                </div>
            </div>
        </header>
    )
}

export default Header;