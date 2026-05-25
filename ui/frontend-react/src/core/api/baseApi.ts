import { createApi } from "@reduxjs/toolkit/query/react";
import { zodBaseQuery } from "./zodBaseQuery";

export const baseApi = createApi({
  reducerPath: "api",
  baseQuery: zodBaseQuery("http://localhost:5173/api/"),
  endpoints: () => ({})
});
