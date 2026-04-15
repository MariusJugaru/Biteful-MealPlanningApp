import { Link } from "react-router-dom";

type NavButtonProps = {
  to: string;
  children: React.ReactNode;
};

function NavButton({ to, children } : NavButtonProps) {
    return (
        <Link
            to={to}
            className="flex items-center gap-2 px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-200"
        >
            {children}
        </Link>
    );
}

export default NavButton;

