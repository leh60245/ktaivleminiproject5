<template>
  <div class="pa-4" style="background-color: #fdfdfd; min-height: 100%;">
    <v-list nav dense>

      <!-- 작가 메뉴 -->
      <v-subheader class="mt-2">📘 작가</v-subheader>

      <v-list-item to="/writer" link>
        <v-list-item-title>작가 대시보드</v-list-item-title>
      </v-list-item>

      <v-list-item to="/writer/write" link>
        <v-list-item-title>글 작성</v-list-item-title>
      </v-list-item>

      <v-list-item to="/ai-preview" link>
        <v-list-item-title>AI 미리보기</v-list-item-title>
      </v-list-item>

      <v-divider class="my-4" />

      <!-- 구독자 메뉴 -->
      <v-subheader class="mt-4">📖 구독자</v-subheader>

      <v-list-item to="/subscriber" link>
        <v-list-item-title>구독자 대시보드</v-list-item-title>
      </v-list-item>

      <v-list-item to="/subscriber/read" link>
        <v-list-item-title>도서 열람</v-list-item-title>
      </v-list-item>

    </v-list>
  </div>
</template>
