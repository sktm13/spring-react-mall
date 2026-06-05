import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { getCartItems, postChangeCart } from "../api/cartApi";

export const getCartItemsAsync = createAsyncThunk("getCartItemsAsync", () => {
    return getCartItems();
});

export const postChangeCartAsync = createAsyncThunk(
    "postChangeCartAsync",
    (param: CartItemRequest) => {
        return postChangeCart(param);
    }
);

// 초기상태
const initState: CartItemsArray = { items: [], status: "" };

const toCartItemsArray = (payload: unknown): CartItemsArray["items"] => {
    if (Array.isArray(payload)) {
        return payload;
    }

    if (
        payload &&
        typeof payload === "object" &&
        "items" in payload &&
        Array.isArray((payload as { items: unknown }).items)
    ) {
        return (payload as CartItemsArray).items;
    }

    return [];
};

const cartSlice = createSlice({
    name: "cartSlice",
    initialState: initState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(getCartItemsAsync.fulfilled, (_state, action) => {
                console.log("getCartItemsAsync payload", action.payload);

                return {
                    items: toCartItemsArray(action.payload),
                    status: "fulfilled",
                };
            })

            .addCase(getCartItemsAsync.pending, (state, _action) => {
                state.status = "pending";
            })

            .addCase(getCartItemsAsync.rejected, (state, _action) => {
                state.items = [];
                state.status = "rejected";
            })

            .addCase(postChangeCartAsync.fulfilled, (_state, action) => {
                console.log("postChangeCartAsync payload", action.payload);

                return {
                    items: toCartItemsArray(action.payload),
                    status: "fulfilled",
                };
            })

            .addCase(postChangeCartAsync.pending, (state, _action) => {
                state.status = "pending";
            })

            .addCase(postChangeCartAsync.rejected, (state, _action) => {
                state.items = [];
                state.status = "rejected";
            });
    },
});

export default cartSlice.reducer;