<template>
  <v-app style="background-color: aliceblue">
    <v-navigation-drawer v-model="drawer" app>
      <v-icon>mdi-cart</v-icon> Ваша корзина:
    </v-navigation-drawer>

    <v-app-bar app>
      <v-app-bar-nav-icon @click="drawer = !drawer">
      </v-app-bar-nav-icon>

      <v-toolbar-title>Travel constructor</v-toolbar-title>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <v-row dense>
          <v-col v-for="provider in ser_providers" :key="provider" cols="3">
            <v-card min-height="150" min-width="300">
              <p class="text-xl-h4 align-center text-center">{{provider}}</p>
              <v-btn color="red lighten-2" align-center dark @click="get_services(provider)">
                {{ hidden ? 'Открыть' : 'Закрыть' }}
              </v-btn>

            </v-card>


          </v-col>
        </v-row>
      </v-container>
      <v-container fluid>
        <v-row dense>
          <v-card v-for="service in provider_services" :key="service" class="mx-auto my-12" max-width="374">
            <v-card-title>{{service.name}}</v-card-title>
            <v-card-text>
              <div class="my-4 text-subtitle-1">
                <v-icon>mdi-currency-rub</v-icon> • {{service.price}}
              </div>
            </v-card-text>
            <v-card-title>Доступное время</v-card-title>
            <v-card-text>
              <v-chip-group v-model="selection" active-class="deep-purple accent-4 white--text" column>
                <v-chip v-for="time in service.availableTimes" :key="time">{{time | formatDate}}</v-chip>
              </v-chip-group>
            </v-card-text>
            <v-card-actions>
              <v-btn color="deep -purple lighten-2" text @click="add_to_cart()">
                Добавить в корзину
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-row>
      </v-container>



      <v-card class="mx-auto my-12" max-width="374">
        <v-card-title>Полёт на воздушном шаре</v-card-title>
        <v-card-text>
          <div class="my-4 text-subtitle-1">
            <v-icon>mdi-currency-rub</v-icon> • 3 000
          </div>
          <div>Просто описание полёта</div>
        </v-card-text>
        <v-divider class="mx-4"></v-divider>
        <v-card-title>Доступное время</v-card-title>
        <v-card-text>
          <v-chip-group v-model="selection" active-class="deep-purple accent-4 white--text" column>
            <v-chip>5:30PM</v-chip>

            <v-chip>7:30PM</v-chip>

            <v-chip>8:00PM</v-chip>

            <v-chip>9:00PM</v-chip>
          </v-chip-group>
        </v-card-text>
        <v-card-actions>
          <v-btn color="deep-purple lighten-2" text @click="add_to_cart()">
            Добавить в корзину
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-main>
  </v-app>
</template>

<script>
import moment from 'moment';

  export default {
    data: () => ({
      hidden: true,
      drawer: null,
      provider_dialog: false,
      ser_providers: null,
      provider_services: null
    }),
    mounted() {
      this.$http.get("http://localhost:8080/api/providers").then(response => {
        this.ser_providers = response.data;
      }, () => {
        alert("Error occured during loading providers");
      })
    },
    methods: {
      get_services(provider) {
        if (this.hidden === true) {
          console.log("getting services");
          this.$http.get("http://localhost:8080/api/servicesOf/" + provider).then(response => {
            this.provider_services = response.data;
            console.log(response.data);
            this.hidden = false;
          }, () => {
            alert("Error occured during loading services of provider " + provider);
          })
        } else {
          this.provider_services = null;
          this.hidden = true;
        }

      },
      add_to_cart() {

      }
    },
    filters: {
      formatDate: function (value) {
        if (value) {
      return moment(String(value)).format('D MMMM hh:mm')
      }
      }
    }

    
  };
</script>