import useCustomLogin from "../hooks/useCustomLogin";

function AboutPage() {

    const { loginStatus, moveToLoginReturn } = useCustomLogin()

    if (loginStatus !== 'fulfilled' && loginStatus !== 'saved') {
        return moveToLoginReturn()
    }

    return (
        <div className="max-w-2xl mx-auto py-10 px-4">

            {/* 제목 */}
            <h1 className="text-2xl font-bold mb-6">
                프로젝트 소개
            </h1>

            {/* 한줄 설명 */}
            <p className="text-gray-700 mb-8">
                Spring Boot와 React 기반으로 구현한 쇼핑몰 프로젝트로,
                JWT 인증과 상태 관리, API 설계 전반을 경험하기 위해 개발했습니다.
            </p>

            {/* 핵심 포인트 */}
            <div className="space-y-6">

                <div>
                    <h2 className="font-semibold mb-1">주요 기능</h2>
                    <p className="text-gray-600 text-sm">
                        로그인 인증, 상품 조회, 장바구니 관리 기능 구현
                    </p>
                </div>

                <div>
                    <h2 className="font-semibold mb-1">기술 스택</h2>
                    <p className="text-gray-600 text-sm">
                        Spring Boot · React · TypeScript · Redux Toolkit
                    </p>
                </div>

                <div>
                    <h2 className="font-semibold mb-2">구현 포인트</h2>

                    <ul className="text-gray-600 text-sm space-y-1 list-disc list-inside">
                        <li>Spring Security + JWT 인증 흐름 및 API 로그인 (Kakao)</li>
                        <li>쿠키 기반 인증 상태 유지</li>
                        <li>페이징 처리 게시판 기능 (Product, Inquiry)</li>
                        <li>사용자별 장바구니 구현</li>
                    </ul>

                    <div className="mt-4">
                        <h3 className="text-sm font-semibold text-gray-800 mb-1">
                            확장 기능
                        </h3>
                        <ul className="text-gray-600 text-sm space-y-1 list-disc list-inside">
                            <li>로그인 실패 시나리오 구현</li>
                            <li>UI 개선</li>
                            <li>권한 기반 문의 답변 기능 (@PreAuthorize)</li>
                        </ul>
                    </div>
                </div>

            </div>

        </div>
    );
}

export default AboutPage;