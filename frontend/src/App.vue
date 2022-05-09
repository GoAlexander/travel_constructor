<template>
  <v-app style="background-color: aliceblue">
    <v-navigation-drawer v-model="drawer" app>
      <v-icon>mdi-cart</v-icon> Ваша корзина:
      <v-list-item v-for="item in cart" :key="item" two-line>
        <v-list-item-content>
          <v-list-item-title>{{item.name}}</v-list-item-title>
          <v-list-item-subtitle>{{item.selectedTime | formatDate}}</v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
      <p v-if="cart.length > 0" class="text-center">
        <v-btn dark color="blue lighten-2" @click="getOrder()">Оформить заказ</v-btn>
        <v-btn dark color="red ligthen-2" @click="clearCart()"> Очистить корзину</v-btn>
      </p>
    </v-navigation-drawer>

    <v-app-bar app>
      <v-app-bar-nav-icon @click="drawer = !drawer">
      </v-app-bar-nav-icon>

      <v-toolbar-title>Travel constructor</v-toolbar-title>
    </v-app-bar>

    <v-main>
      <v-container fluid>
        <v-row dense>
          <v-col v-for="provider in serProviders" :key="provider.name" cols="3">
            <v-card min-height="150" min-width="200">
              <p class="text-xl-h4 align-center text-center">{{provider}}</p>
              <p class="text-center">
                <v-btn color="blue lighten-2" align-center dark @click="getServices(provider)">
                  {{ hidden ? 'Открыть' : 'Закрыть' }}
                  </v-btn>
              </p>

            </v-card>


          </v-col>
        </v-row>
      </v-container>
      <v-container fluid>
        <v-row dense>
          <v-card v-for="service in providerServices" :key="service.name" class="mx-auto my-12" max-width="374">
            <v-card-title>{{service.name}}</v-card-title>
            <v-card-text>
              <div v-if="service.price != null" class="my-4 text-subtitle-1">
                <v-icon>mdi-currency-rub</v-icon> • {{service.price}}
              </div>
            </v-card-text>
            <v-card-title>Доступное время</v-card-title>
            <v-card-text >
              <v-chip-group v-model="service.availableTimes.selectedTime" active-class="deep-purple accent-4 white--text" column>
                <v-chip v-for="time in service.availableTimes" :key="time">{{time | formatDate}}</v-chip>
              </v-chip-group>
            </v-card-text>
            <v-card-actions>
              <v-spacer />
              <v-btn color="deep-purple lighten-2" text
                @click="addToCart(service, service.availableTimes[service.availableTimes.selectedTime])">
                Добавить в корзину
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-row>
      </v-container>

    </v-main>
  </v-app>
</template>

<script>
  import moment from 'moment';

  export default {
    data: () => ({
      selections:[],
      hidden: true,
      drawer: null,
      providerDialog: false,
      serProviders: null,
      providerServices: null,
      cart: []
    }),
    mounted() {
      this.$http.get("http://localhost:8080/api/providers").then(response => {
        this.serProviders = response.data;
      }, () => {
        alert("Error occured during loading providers");
      })
    },
    methods: {
      getServices(provider) {
        if (this.hidden === true) {
          console.log("getting services");
          this.$http.get("http://localhost:8080/api/servicesOf/" + provider).then(response => {
            this.providerServices = response.data.map(v => ({...v, selectedTime: null}));
            console.log(this.providerServices);
            this.hidden = false;
          }, () => {
            alert("Error occured during loading services of provider " + provider);
          })
        } else {
          this.providerServices = null;
          this.hidden = true;
        }

      },
      addToCart(service, selection) {
        const newService = {
          name: service.name,
          selectedTime: selection
        };
        this.$http.post("http://localhost:8080/api/addServiceToCart", newService).then(response => {
          console.log("Successfully sent new item to backend: " + JSON.stringify(response.data));
        }, () => {
          alert("Error occured during sending new item to backend");
        });
        this.cart.push(newService);
        console.log(this.cart);

      },
      getOrder() {
        this.$http.get("http://localhost:8080/api/getOrder").then(response => {
          console.log(response.bodyText);
          alert(response.bodyText);
          this.cart = [];
        }, () => {
          alert("Error occured during order creation");
        })
      },
      clearCart() {
        this.cart = [];
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