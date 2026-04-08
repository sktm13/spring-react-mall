import LogoutComponent from "../../components/member/logoutComponent"
import BasicMenu from "../../components/menus/basicMenu"

function LogoutPage() {

    return (
        <div className="min-h-screen bg-gray-100">
            <BasicMenu />

            <div className="flex justify-center items-center h-[calc(100vh-64px)]">
                <LogoutComponent />
            </div>
        </div>
    )
}
export default LogoutPage
