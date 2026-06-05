import useCustomCart from "../../hooks/useCustomCart";
import CartItemComponent from "../cart/cartItemComponent";

const CartComponent = () => {
  const { loginStatus, cartItems, changeCart } = useCustomCart();

  const items = Array.isArray(cartItems.items) ? cartItems.items : [];

  const totalPrice = items.reduce(
    (sum, item) => sum + item.price * item.qty,
    0
  );

  return (
    <div className="w-full max-w-7xl mx-auto px-6 py-6 bg-white rounded-2xl shadow">
      {/* 로그인 안했을 때 */}
      {!loginStatus && (
        <div className="text-center py-10 text-gray-400">
          로그인 후 이용해주세요
        </div>
      )}

      {/* 로그인 했을 때 */}
      {loginStatus && (
        <>
          {/* 로딩 */}
          {cartItems.status === "pending" && (
            <div className="text-center py-10">Loading...</div>
          )}

          {/* 데이터 */}
          {cartItems.status === "fulfilled" && (
            <>
              {/* 헤더 */}
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-2xl font-bold">Cart</h2>

                <div className="text-sm text-gray-500">
                  총 {items.length}개 상품
                </div>
              </div>

              {/* 리스트 */}
              {items.length === 0 ? (
                <div className="text-center py-10 text-gray-400">
                  장바구니가 비어있습니다
                </div>
              ) : (
                <ul className="space-y-4">
                  {items.map((item) => (
                    <CartItemComponent
                      key={item.cino}
                      cartItem={item}
                      changeCart={changeCart}
                    />
                  ))}
                </ul>
              )}

              {/* 총 금액 */}
              {items.length > 0 && (
                <div className="mt-8 border-t pt-6 flex justify-between items-center">
                  <div className="text-lg font-semibold">총 금액</div>

                  <div className="text-xl font-bold">
                    {totalPrice.toLocaleString()}원
                  </div>
                </div>
              )}
            </>
          )}

          {cartItems.status === "rejected" && (
            <div className="text-center py-10 text-gray-400">
              장바구니 정보를 불러오지 못했습니다.
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default CartComponent;