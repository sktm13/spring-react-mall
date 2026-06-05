# =========================
# Build Stage
# =========================
FROM node:22-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY . .

ARG VITE_API_SERVER
ARG VITE_FRONTEND_HOST
ARG VITE_KAKAO_REST_API_KEY
ARG VITE_KAKAO_REDIRECT_URI

ENV VITE_API_SERVER=$VITE_API_SERVER
ENV VITE_FRONTEND_HOST=$VITE_FRONTEND_HOST
ENV VITE_KAKAO_REST_API_KEY=$VITE_KAKAO_REST_API_KEY
ENV VITE_KAKAO_REDIRECT_URI=$VITE_KAKAO_REDIRECT_URI

RUN npm run build


# =========================
# Run Stage
# =========================
FROM nginx:alpine

COPY --from=build /app/dist /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]