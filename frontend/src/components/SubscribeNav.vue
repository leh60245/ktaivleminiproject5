<template>
  <v-list nav dense>

    <v-subheader>📖 구독자 메뉴</v-subheader>

    <v-list-item to="/subscriber" link>
      <v-list-item-title>🏠 구독자 대시보드</v-list-item-title>
    </v-list-item>

    <v-list-item to="/subscriber/read" link>
      <v-list-item-title>📚 도서 열람</v-list-item-title>
    </v-list-item>

    <!-- 추후 확장 가능
    <v-list-item to="/subscriber/points" link>
      <v-list-item-title>💰 포인트 내역</v-list-item-title>
    </v-list-item>
    -->

  </v-list>
</template>

<script setup>
</script>
