package com.superBoy.FoodieHub.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI foodieHubOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FoodieHub API")
                        .description("""
                                ## Online Restaurant Ordering Platform
                                
                                FoodieHub is a full-featured restaurant backend system that supports:
                                - **Customer** registration, profile management, and ordering
                                - **Menu & Category** management with image uploads (AWS S3)
                                - **Order lifecycle** tracking (create → dispatch → deliver)
                                - **Supplier** management and procurement workflows
                                - **Razorpay** payment gateway for both customer checkouts and supplier payments
                                - **Inventory** control tied to purchase orders
                                
                                > Base URL: `http://localhost:8080`
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("FoodieHub Team")
                                .email("support@foodiehub.com")
                                .url("https://github.com/superBoy/FoodieHub"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ))
                .tags(List.of(
                        new Tag().name("Admin").description("Admin account management"),
                        new Tag().name("Customer").description("Customer registration and profile operations"),
                        new Tag().name("Customer Address").description("Manage delivery addresses for customers"),
                        new Tag().name("Customer Payment").description("Razorpay-powered customer checkout and payment verification"),
                        new Tag().name("Menu").description("Restaurant menu items — add, update, price, discount, availability"),
                        new Tag().name("Category").description("Food categories with optional image upload"),
                        new Tag().name("Order").description("Customer order lifecycle management"),
                        new Tag().name("Order Item").description("Individual line items within an order"),
                        new Tag().name("Supplier").description("Supplier profile management"),
                        new Tag().name("Supplier Address").description("Manage addresses for suppliers"),
                        new Tag().name("Supplier Payment").description("Razorpay-based supplier payment gateway"),
                        new Tag().name("Purchase Order").description("Procurement / purchase order management"),
                        new Tag().name("Purchase Item").description("Line items within a purchase order"),
                        new Tag().name("Inventory").description("Stock levels and inventory tracking")
                ));
    }
}
