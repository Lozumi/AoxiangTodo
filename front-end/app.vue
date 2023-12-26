<template>
  <NConfigProvider
      :locale="zhCN"
      :date-locale="dateZhCN"
      :theme-overrides="themeOverrides"
      inline-theme-disabled
  >
    <nuxtLayout>
      <NuxtPage />
    </nuxtLayout>
  </NConfigProvider>
</template>
<script setup>
import { zhCN, dateZhCN, NConfigProvider } from "naive-ui";

/**
 * js 文件下使用这个做类型提示
 * @type import('naive-ui').GlobalThemeOverrides
 */
const themeOverrides = {
  common: {
    primaryColor: "#FC8952",
    primaryColorHover: "#FC8952",
    primaryColorPressed: "#FC8952",
    primaryColorSuppl: "#FC8952",
  },
  Button: {
    textColor: "#FC8952",
    border: "1px solid #FF763B",
  }
};
</script>
