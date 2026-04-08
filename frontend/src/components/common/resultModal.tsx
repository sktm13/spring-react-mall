function ResultModal({ title, content, callbackFn }: ResultModal) {
    return (
        <div
            className="fixed inset-0 z-50 flex items-center justify-center bg-black/50"
            onClick={() => {
                if (callbackFn) callbackFn();
            }}
        >
            <div
                className="bg-white w-full max-w-sm rounded-2xl shadow-xl p-6"
                onClick={(e) => e.stopPropagation()}
            >
                {/* 타이틀 */}
                <h2 className="text-lg font-semibold mb-4 text-center">
                    {title}
                </h2>

                {/* 내용 */}
                <p className="text-gray-700 text-center mb-6">
                    {content}
                </p>

                {/* 버튼 */}
                <div className="flex justify-center">
                    <button
                        className="px-6 py-2 bg-black text-white rounded-lg font-semibold hover:bg-gray-800 transition"
                        onClick={() => {
                            if (callbackFn) callbackFn();
                        }}
                    >
                        확인
                    </button>
                </div>
            </div>
        </div>
    );
}

export default ResultModal;