#include <drogon/drogon.h>
#include <iostream>

int main() {
    // Înregistrăm ruta manual aici ca să evităm erorile de Linker
    drogon::app().registerHandler("/user", [](const drogon::HttpRequestPtr& req,
        std::function<void(const drogon::HttpResponsePtr&)>&& callback) {

            Json::Value data;
            data["result"] = "success";
            data["message"] = "Salut! Serverul functioneaza direct din Main.";

            auto resp = drogon::HttpResponse::newHttpJsonResponse(data);
            callback(resp);
        });

    std::cout << "Serverul porneste pe portul 8080..." << std::endl;

    drogon::app().addListener("0.0.0.0", 8080);
    drogon::app().run();

    return 0;
}