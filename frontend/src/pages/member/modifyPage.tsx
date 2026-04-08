import BasicMenu from "../../components/menus/basicMenu"
import ModifyComponent from "../../components/member/modifyComponent"

function ModifyPage() {

    return (
        <div className="min-h-screen bg-gray-100">
            <BasicMenu />

            <div className="flex justify-center items-start pt-10 px-4">
                <div className="w-full flex justify-center">
                    <ModifyComponent />
                </div>
            </div>
        </div>
    )
}

export default ModifyPage
