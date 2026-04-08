import { useDispatch } from "react-redux";
import LoginComponent from "../../components/member/loginComponent";
import BasicMenu from "../../components/menus/basicMenu";
import type { AppDispatch } from "../../store";
import { useEffect } from "react";
import { reset } from "../../slices/loginSlice";

function LoginPage() {
    const dispatch = useDispatch<AppDispatch>()
    useEffect(() => { dispatch(reset()) }, [dispatch])
    return (
        <div className="min-h-screen bg-gray-100">
            <BasicMenu />

            <div className="flex justify-center items-center h-[calc(100vh-64px)]">
                <LoginComponent />
            </div>
        </div>
    )
} export default LoginPage;