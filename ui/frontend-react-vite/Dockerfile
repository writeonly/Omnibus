# Stage 1: build
FROM node:20-alpine AS builder

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .

RUN npm run build


# Stage 2: nginx runtime
FROM nginx:alpine

# usuń domyślną stronę nginx
RUN rm -rf /usr/share/nginx/html/*

# skopiuj build
COPY --from=builder /app/dist /usr/share/nginx/html

# SPA fallback (React Router)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
