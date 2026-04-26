FROM node:20-alpine AS build

WORKDIR /app

COPY package.json package-lock.json angular.json tsconfig.json tsconfig.app.json ./
RUN npm ci

COPY src ./src
RUN npm run build


FROM nginx:1.27-alpine

COPY --from=build /app/dist/omnibus-frontend/browser /usr/share/nginx/html

EXPOSE 80
