<template>
  <v-card
      class="list-card"
      max-width="700"
  >


    <v-list lines="two">
      <v-list-subheader>
          Things To Do
      </v-list-subheader>

    </v-list>

    <v-divider></v-divider>

    <v-list lines="two" select-strategy="classic" class="list1">
      <v-list-subheader>General</v-list-subheader>

      <v-list-item value="notifications">
        <template v-slot:prepend="{ isActive }">

          <v-list-item-action start>
            <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
          </v-list-item-action>
        </template>

        <v-list-item-title>Notifications</v-list-item-title>

        <v-list-item-subtitle>
          Notify me about updates to apps or games that I downloaded
        </v-list-item-subtitle>
      </v-list-item>

      <v-list-item value="sound" class="custom-list1">
        <template v-slot:prepend="{ isActive }">
          <v-list-item-action start>
            <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
          </v-list-item-action>
        </template>

        <v-list-item-title>
          Sound
        </v-list-item-title>

        <v-list-item-subtitle>
          Auto-update apps at any time. Data charges may apply
        </v-list-item-subtitle>
      </v-list-item>

      <v-list-item value="widgets">
        <template v-slot:prepend="{ isActive }">
          <v-list-item-action start>
            <v-checkbox-btn :model-value="isActive"></v-checkbox-btn>
          </v-list-item-action>
        </template>

        <v-list-item-title>Auto-add widgets</v-list-item-title>

        <v-list-item-subtitle>
          Automatically add home screen widgets when downloads complete
        </v-list-item-subtitle>
      </v-list-item>
    </v-list>
  </v-card>
</template>

<style>
.list1{
  border:1px solid #303030;

}
</style>

<style scoped>
.list-card {
  margin-left:300px;
}
</style>

<script setup lang="ts">
</script>
