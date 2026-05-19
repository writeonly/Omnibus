FROM node:20-alpine AS build

WORKDIR /app

COPY package.json package-lock.json angular.json tsconfig*.json ./
RUN npm ci

COPY src ./src
RUN npm run build


FROM nginx:1.27-alpine

COPY --from=build /app/dist/omnibus-frontend/browser /usr/share/nginx/html

# 👇 dodaj assets
COPY src/assets/env.template.js /usr/share/nginx/html/assets/env.template.js
COPY nginx/default.conf /etc/nginx/conf.d/default.conf

# 👇 entrypoint
COPY nginx/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
