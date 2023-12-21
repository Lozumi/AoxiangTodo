<template>
  <v-card>
    <v-tabs
        v-model="tab"
        bg-color="primary"
    >
      <v-tab value="one">Item One</v-tab>
      <v-tab value="two">Item Two</v-tab>
    </v-tabs>

    <v-card-text>
      <v-window v-model="tab">
        <v-window-item value="one">
          登录
        </v-window-item>

        <v-window-item value="two">
         注册
        </v-window-item>

      </v-window>
    </v-card-text>
  </v-card>

  <div>
    <v-text-field
        label="Main input"
        :rules="rules"
        hide-details="auto"
    ></v-text-field>
    <v-text-field label="Another input"></v-text-field>
  </div>


</template>

<script>
export default {
  data: () => ({
    rules: [
      value => !!value || 'Required.',
      value => (value && value.length >= 3) || 'Min 3 characters',
    ],
    tab: null,
  }),
}

</script>