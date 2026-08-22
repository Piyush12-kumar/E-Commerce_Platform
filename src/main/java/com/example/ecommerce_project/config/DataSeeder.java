package com.example.ecommerce_project.config;

import com.example.ecommerce_project.Dao.CategoryRepo;
import com.example.ecommerce_project.Dao.ProductRepo;
import com.example.ecommerce_project.model.Category;
import com.example.ecommerce_project.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public void run(String... args) {
        // Only seed if no products exist
        if (productRepo.count() > 0) {
            return;
        }

        // Create Categories
        Category electronics = createCategory("Electronics", "Smartphones, laptops, tablets, and other electronic gadgets", "https://images.unsplash.com/photo-1498049794561-7780e7231661?w=400");
        Category clothing = createCategory("Clothing", "Men and women fashion, apparel, and accessories", "https://images.unsplash.com/photo-1445205170230-053b83016050?w=400");
        Category homeKitchen = createCategory("Home & Kitchen", "Furniture, appliances, and home decor items", "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400");
        Category books = createCategory("Books", "Fiction, non-fiction, educational, and self-help books", "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400");
        Category sports = createCategory("Sports & Outdoors", "Sports equipment, fitness gear, and outdoor accessories", "https://images.unsplash.com/photo-1461896836934-bd45ba688b23?w=400");
        Category beauty = createCategory("Beauty & Personal Care", "Skincare, makeup, haircare, and grooming products", "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=400");
        Category toys = createCategory("Toys & Games", "Toys, board games, puzzles, and kids entertainment", "https://images.unsplash.com/photo-1558060318-e0a78f8d730d?w=400");
        Category groceries = createCategory("Groceries & Food", "Organic food, snacks, beverages, and pantry essentials", "https://images.unsplash.com/photo-1542838132-92c53300491e?w=400");

        categoryRepo.saveAll(List.of(electronics, clothing, homeKitchen, books, sports, beauty, toys, groceries));

        // Electronics Products
        productRepo.saveAll(List.of(
            createProduct("Apple iPhone 15 Pro", "Latest iPhone with A17 Pro chip, 48MP camera system, titanium design, and USB-C connectivity. 256GB storage.", 1199.99, 1099.99, 50, "ELEC-IPH15PRO", "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400", electronics, true),
            createProduct("Samsung Galaxy S24 Ultra", "Premium Android flagship with S Pen, 200MP camera, Snapdragon 8 Gen 3, and 6.8 inch Dynamic AMOLED display.", 1299.99, 1199.99, 35, "ELEC-SGS24U", "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400", electronics, true),
            createProduct("MacBook Pro 16 M3 Max", "Apple MacBook Pro with M3 Max chip, 36GB RAM, 1TB SSD, Liquid Retina XDR display, and 22-hour battery life.", 3499.99, 3299.99, 20, "ELEC-MBP16M3", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400", electronics, true),
            createProduct("Sony WH-1000XM5 Headphones", "Industry-leading noise canceling wireless headphones with 30-hour battery life and crystal clear calls.", 349.99, 299.99, 100, "ELEC-SONYXM5", "https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=400", electronics, false),
            createProduct("Dell XPS 15 Laptop", "15.6 inch OLED display, Intel Core i9, 32GB RAM, 1TB SSD. Perfect for creators and professionals.", 1899.99, 1749.99, 25, "ELEC-DELLXPS15", "https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=400", electronics, false),
            createProduct("iPad Air M2", "11-inch Liquid Retina display, M2 chip, 128GB storage, supports Apple Pencil Pro and Magic Keyboard.", 599.99, 549.99, 60, "ELEC-IPADAIRM2", "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400", electronics, false),
            createProduct("Nintendo Switch OLED", "7-inch OLED screen, enhanced audio, 64GB internal storage, wide adjustable stand, wired LAN port.", 349.99, 319.99, 45, "ELEC-NSWITCH", "https://images.unsplash.com/photo-1578303512597-81e6cc155b3e?w=400", electronics, false)
        ));

        // Clothing Products
        productRepo.saveAll(List.of(
            createProduct("Nike Air Max 270 Sneakers", "Lightweight and comfortable sneakers with Max Air unit for all-day cushioning. Available in multiple colors.", 150.00, 129.99, 80, "CLO-NIKEAM270", "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400", clothing, true),
            createProduct("Levi's 501 Original Fit Jeans", "Classic straight-leg jeans with signature button fly. 100% cotton denim, timeless American style.", 89.99, 69.99, 120, "CLO-LEVIS501", "https://images.unsplash.com/photo-1542272604-787c3835535d?w=400", clothing, false),
            createProduct("Ralph Lauren Polo Shirt", "Classic fit polo shirt in breathable cotton mesh with signature embroidered pony. Perfect for casual wear.", 98.50, 79.99, 90, "CLO-RLPOLO", "https://images.unsplash.com/photo-1625910513413-5fc42c38d3e1?w=400", clothing, true),
            createProduct("North Face Puffer Jacket", "Warm 700-fill goose down insulated jacket with water-repellent finish. Ideal for cold weather adventures.", 299.99, 249.99, 40, "CLO-NFPUFFER", "https://images.unsplash.com/photo-1544923246-77307dd270cf?w=400", clothing, false),
            createProduct("Adidas Ultraboost Running Shoes", "Premium running shoes with Boost midsole technology for incredible energy return and comfort.", 189.99, 159.99, 65, "CLO-ADIUB", "https://images.unsplash.com/photo-1556906781-9a412961c28c?w=400", clothing, false),
            createProduct("Formal Business Suit - Navy Blue", "Tailored slim-fit two-piece suit in premium wool blend. Includes jacket and trousers. Perfect for office.", 450.00, 379.99, 30, "CLO-SUIT-NB", "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=400", clothing, false)
        ));

        // Home & Kitchen Products
        productRepo.saveAll(List.of(
            createProduct("KitchenAid Stand Mixer", "Artisan Series 5-quart tilt-head stand mixer with 10 speeds. Includes flat beater, dough hook, and wire whip.", 449.99, 379.99, 30, "HK-KAMIXER", "https://images.unsplash.com/photo-1594385208974-2f8bb07ccc07?w=400", homeKitchen, true),
            createProduct("Dyson V15 Detect Vacuum", "Cordless vacuum with laser dust detection, piezo sensor, and up to 60 minutes runtime. HEPA filtration.", 749.99, 649.99, 25, "HK-DYSONV15", "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=400", homeKitchen, true),
            createProduct("Instant Pot Duo 7-in-1", "Electric pressure cooker, slow cooker, rice cooker, steamer, saute pan, yogurt maker, and warmer. 6-quart.", 89.99, 69.99, 75, "HK-INSTPOT", "https://images.unsplash.com/photo-1585515320310-259814833e62?w=400", homeKitchen, false),
            createProduct("Memory Foam Queen Mattress", "12-inch gel-infused memory foam mattress with cooling technology. CertiPUR-US certified, medium firm.", 599.99, 499.99, 15, "HK-MATTRESS-Q", "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400", homeKitchen, false),
            createProduct("Ninja Professional Blender", "1100-watt professional blender with Total Crushing technology. 72oz pitcher, perfect for smoothies and soups.", 99.99, 79.99, 55, "HK-NINJABLND", "https://images.unsplash.com/photo-1570222094114-d054a817e56b?w=400", homeKitchen, false),
            createProduct("Modern L-Shaped Sectional Sofa", "Contemporary sectional sofa with chaise lounge, premium linen fabric, and solid wood frame. Seats 4-5 people.", 1299.99, 1099.99, 10, "HK-SOFA-L", "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400", homeKitchen, false)
        ));

        // Books Products
        productRepo.saveAll(List.of(
            createProduct("Atomic Habits by James Clear", "An easy and proven way to build good habits and break bad ones. #1 New York Times bestseller.", 16.99, 11.99, 200, "BK-ATOMHAB", "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=400", books, true),
            createProduct("The Psychology of Money", "Timeless lessons on wealth, greed, and happiness by Morgan Housel. A must-read for financial literacy.", 14.99, 10.99, 150, "BK-PSYMONEY", "https://images.unsplash.com/photo-1592496431122-2349e0fbc666?w=400", books, false),
            createProduct("Clean Code by Robert C. Martin", "A handbook of agile software craftsmanship. Essential reading for every professional programmer.", 39.99, 32.99, 80, "BK-CLEANCODE", "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=400", books, true),
            createProduct("Dune by Frank Herbert", "The epic science fiction masterpiece. A stunning blend of adventure and mysticism, set on the desert planet Arrakis.", 12.99, 9.99, 100, "BK-DUNE", "https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=400", books, false),
            createProduct("The Alchemist by Paulo Coelho", "A magical story about following your dreams. One of the best-selling books in history, translated into 80 languages.", 13.99, 9.99, 180, "BK-ALCHEMIST", "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400", books, false),
            createProduct("System Design Interview", "Step-by-step guide for system design interviews by Alex Xu. Covers scalable system architecture and design patterns.", 35.99, 29.99, 60, "BK-SYSDESIGN", "https://images.unsplash.com/photo-1532012197267-da84d127e765?w=400", books, false)
        ));

        // Sports & Outdoors Products
        productRepo.saveAll(List.of(
            createProduct("Peloton Bike+", "Indoor exercise bike with 23.8 inch rotating HD touchscreen, auto-follow resistance, and thousands of live classes.", 2495.00, 2295.00, 10, "SP-PELOTON", "https://images.unsplash.com/photo-1591291621164-2c6367723315?w=400", sports, true),
            createProduct("YETI Tundra 45 Cooler", "Premium hard cooler with rotomolded construction, 3 inch PermaFrost insulation. Keeps ice for days. Bear-resistant.", 325.00, 299.99, 30, "SP-YETI45", "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400", sports, false),
            createProduct("Bowflex SelectTech 552 Dumbbells", "Adjustable dumbbells replacing 15 sets of weights (5-52.5 lbs each). Space-saving and easy dial adjustment.", 429.99, 379.99, 20, "SP-BFLEX552", "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=400", sports, true),
            createProduct("Coleman 8-Person Tent", "WeatherTec waterproof tent with room divider, fits 2 queen air mattresses. Setup in 9 minutes.", 219.99, 179.99, 35, "SP-COLMNTENT", "https://images.unsplash.com/photo-1504280390367-361c6d9f38f4?w=400", sports, false),
            createProduct("Garmin Forerunner 965 GPS Watch", "Premium GPS running/triathlon smartwatch with AMOLED display, advanced training metrics, and music storage.", 599.99, 549.99, 40, "SP-GARMIN965", "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400", sports, false),
            createProduct("Yoga Mat Premium 6mm", "Non-slip eco-friendly TPE yoga mat with alignment markings. Includes carrying strap. Perfect for all practices.", 39.99, 29.99, 150, "SP-YOGAMAT", "https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?w=400", sports, false)
        ));

        // Beauty & Personal Care Products
        productRepo.saveAll(List.of(
            createProduct("Dyson Airwrap Multi-Styler", "Complete styling tool that curls, waves, smooths, and dries. Coanda airflow technology, multiple attachments.", 599.99, 549.99, 25, "BP-DYSAIRWRAP", "https://images.unsplash.com/photo-1522338242992-e1a54571e7a4?w=400", beauty, true),
            createProduct("CeraVe Moisturizing Cream", "Daily face and body moisturizer with 3 essential ceramides and hyaluronic acid. Fragrance-free, 19oz.", 18.99, 15.99, 200, "BP-CERAVE", "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=400", beauty, false),
            createProduct("Olaplex Hair Repair Treatment Set", "Complete bond repair system including No.3, No.4, No.5, and No.8. Repairs damaged, broken hair.", 84.99, 69.99, 60, "BP-OLAPLEX", "https://images.unsplash.com/photo-1526947425960-945c6e72858f?w=400", beauty, true),
            createProduct("La Mer Moisturizing Cream", "Luxury moisturizer with Miracle Broth that helps heal dryness, lines, and wrinkles. 2oz jar.", 380.00, 350.00, 15, "BP-LAMER", "https://images.unsplash.com/photo-1571781926291-c477ebfd024b?w=400", beauty, false),
            createProduct("Philips Norelco Shaver 9000", "Premium electric shaver with AI-powered SenseIQ technology, wet and dry use, and 60-min runtime.", 229.99, 199.99, 40, "BP-PHILSHAVER", "https://images.unsplash.com/photo-1585747860036-4cb4e1a543b2?w=400", beauty, false),
            createProduct("SK-II Facial Treatment Essence", "Iconic anti-aging essence with over 90% Pitera. Improves skin texture, firmness, and radiance. 230ml.", 185.00, 165.00, 30, "BP-SKIIFT", "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=400", beauty, false)
        ));

        // Toys & Games Products
        productRepo.saveAll(List.of(
            createProduct("LEGO Star Wars Millennium Falcon", "Ultimate Collector Series set with 7541 pieces. Includes Han Solo, Chewbacca, and crew minifigures.", 849.99, 799.99, 15, "TG-LEGOMF", "https://images.unsplash.com/photo-1587654780291-39c9404d7dd0?w=400", toys, true),
            createProduct("PlayStation 5 DualSense Controller", "Wireless controller with haptic feedback, adaptive triggers, built-in microphone, and USB-C charging.", 69.99, 59.99, 100, "TG-PS5CTRL", "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400", toys, false),
            createProduct("Monopoly Board Game", "Classic family board game of buying, selling, and trading properties. For 2-8 players, ages 8+.", 24.99, 19.99, 80, "TG-MONOPOLY", "https://images.unsplash.com/photo-1563901935883-cb61f6f26757?w=400", toys, false),
            createProduct("DJI Mini 3 Pro Drone", "Lightweight drone under 249g with 4K/60fps video, 48MP photos, obstacle avoidance, and 34-min flight time.", 759.99, 699.99, 20, "TG-DJIMINI3", "https://images.unsplash.com/photo-1473968512647-3e447244af8f?w=400", toys, true),
            createProduct("Rubik's Cube Speed Edition", "Competition-grade 3x3 speed cube with smooth turning mechanism and corner cutting. For beginners to pros.", 12.99, 9.99, 200, "TG-RUBIKS", "https://images.unsplash.com/photo-1591991731833-b4807cf7ef94?w=400", toys, false),
            createProduct("Hot Wheels Ultimate Garage", "Multi-level garage playset with space for 100+ cars, motorized gorilla attack, and helicopter rescue.", 89.99, 74.99, 40, "TG-HWGARAGE", "https://images.unsplash.com/photo-1594787318286-3d835c1d207f?w=400", toys, false)
        ));

        // Groceries & Food Products
        productRepo.saveAll(List.of(
            createProduct("Organic Coffee Beans 2lb", "Single-origin Ethiopian Yirgacheffe whole bean coffee. Light roast, notes of blueberry and dark chocolate.", 24.99, 19.99, 100, "GF-COFFEEBNS", "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=400", groceries, true),
            createProduct("Manuka Honey UMF 15+", "Premium New Zealand Manuka honey with certified UMF 15+ rating. Raw, unpasteurized, 500g jar.", 59.99, 49.99, 50, "GF-MANUKA15", "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=400", groceries, false),
            createProduct("Organic Green Tea Collection", "Assorted organic green tea variety pack - Matcha, Sencha, Jasmine, and Gunpowder. 80 tea bags.", 18.99, 14.99, 120, "GF-GREENTEA", "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400", groceries, false),
            createProduct("Mixed Nuts & Trail Mix Bundle", "Premium assortment of almonds, cashews, walnuts, pecans, and dried cranberries. No added salt, 3lb pack.", 34.99, 28.99, 80, "GF-NUTSMIX", "https://images.unsplash.com/photo-1599599810769-bcde5a160d32?w=400", groceries, false),
            createProduct("Extra Virgin Olive Oil 1L", "Cold-pressed Italian extra virgin olive oil from Tuscany. First harvest, low acidity, rich fruity flavor.", 29.99, 24.99, 70, "GF-OLIVEOIL", "https://images.unsplash.com/photo-1474979266404-7f28eb0d3f59?w=400", groceries, true),
            createProduct("Dark Chocolate Collection Box", "Artisan dark chocolate assortment - 70%, 80%, and 90% cacao. Single-origin beans from Ghana and Ecuador.", 42.99, 36.99, 55, "GF-DARKCHOC", "https://images.unsplash.com/photo-1549007994-cb92caebd54b?w=400", groceries, false)
        ));

        System.out.println("✅ Database seeded with 8 categories and 49 products!");
    }

    private Category createCategory(String name, String description, String imageUrl) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setImageUrl(imageUrl);
        category.setActive(true);
        return category;
    }

    private Product createProduct(String name, String description, double price, double discountPrice, int stock, String sku, String imageUrl, Category category, boolean featured) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(BigDecimal.valueOf(price));
        product.setDiscountPrice(BigDecimal.valueOf(discountPrice));
        product.setStock(stock);
        product.setSku(sku);
        product.setImageURL(imageUrl);
        product.setCategory(category);
        product.setFeatured(featured);
        product.setActive(true);
        return product;
    }
}

