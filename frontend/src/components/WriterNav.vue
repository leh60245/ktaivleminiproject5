<template>
  <v-list nav dense>

    <v-subheader>✍ 작가 메뉴</v-subheader>

    <v-list-item to="/writer" link>
      <v-list-item-title>📚 작가 대시보드</v-list-item-title>
    </v-list-item>

    <v-list-item to="/writer/write" link>
      <v-list-item-title>📝 글 작성</v-list-item-title>
    </v-list-item>

    <v-list-item to="/ai-preview" link>
      <v-list-item-title>🤖 AI 미리보기</v-list-item-title>
    </v-list-item>

  </v-list>
</template>

<script setup>
</script>
