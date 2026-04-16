import { Link } from "react-router-dom";

const variants = {
    default: "hover:bg-gray-200",
    gray: "bg-gray-400 text-white hover:bg-gray-600",
} as const;

type Variant = keyof typeof variants;

type NavButtonProps = {
  to: string;
  children: React.ReactNode;
  variant?: Variant;
};

function NavButton({ to, children, variant = "default" } : NavButtonProps) {
    const base = "flex items-center gap-2 px-3 py-2 rounded-md text-sm font-medium transition";

    return (
        <Link to={to} className={`${base} ${variants[variant]}`}>
            {children}
        </Link>
    );
}

export default NavButton;

