import { NavLink } from "react-router";

function MainPage() {

    return (
        <div className="flex items-center justify-center min-h-[70vh] text-center">

            <div>
                {/* 타이틀 */}
                <h1 className="text-4xl font-bold mb-4">
                    Shopping Mall
                </h1>

                {/* 설명 */}
                <p className="text-gray-500 mb-8">
                    Spring Boot + React 기반 쇼핑몰 프로젝트
                </p>
            </div>

        </div>
    );
}

export default MainPage;