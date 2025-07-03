<script setup>
import { ref } from 'vue'
import RegisterModal from '@/views/RegisterModal.vue'

const avatarBadgeProps = {
  dot: true,
  location: 'bottom right',
  offsetX: 3,
  offsetY: 3,
  color: 'success',
  bordered: true,
}

const userName = "USER"
const registerModalRef = ref()

function openRegisterModal() {
  registerModalRef.value.open()
}
</script>

<template>
  <VBadge v-bind="avatarBadgeProps">
    <VAvatar
      style="cursor: pointer;"
      color="primary"
      variant="tonal"
    >
      <VIcon icon="mdi-account" />

      <!-- SECTION Menu -->
      <VMenu
        activator="parent"
        width="230"
        location="bottom end"
        offset="14px"
      >
        <VList>
          <!-- 👉 User Avatar & Name -->
          <VListItem>
            <template #prepend>
              <VListItemAction start>
                <VBadge v-bind="avatarBadgeProps">
                  <VAvatar
                    color="primary"
                    size="40"
                    variant="tonal"
                  >
                    <VIcon icon="mdi-account" />
                  </VAvatar>
                </VBadge>
              </VListItemAction>
            </template>

            <VListItemTitle class="font-weight-semibold">
              {{ userName }}
            </VListItemTitle>
          </VListItem>

          <VDivider class="my-2" />

          <!-- 👉 Register -->
          <VListItem @click="openRegisterModal">
            <template #prepend>
              <VIcon
                class="me-2"
                icon="mdi-account-plus"
                size="22"
              />
            </template>
            <VListItemTitle>회원가입</VListItemTitle>
          </VListItem>

          <!-- 👉 Logout -->
          <VListItem to="/login">
            <template #prepend>
              <VIcon
                class="me-2"
                icon="mdi-logout-variant"
                size="22"
              />
            </template>
            <VListItemTitle>Logout</VListItemTitle>
          </VListItem>
        </VList>
      </VMenu>
      <!-- !SECTION -->
    </VAvatar>
  </VBadge>

  <!-- 회원가입 모달 컴포넌트 -->
  <RegisterModal ref="registerModalRef" />
</template>