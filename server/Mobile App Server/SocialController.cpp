#include "SocialController.h"

#include <json/json.h>
#include <cstdio>
#include <iostream>
#include <memory>
#include <array>

#ifdef _WIN32
#define POPEN _popen
#define PCLOSE _pclose
#else
#define POPEN popen
#define PCLOSE pclose
#endif

// Simple helper that runs curl and returns stdout as string.
// method: "GET", "POST", "DELETE"
// data: raw payload (for POST use a JSON string or quoted string as needed)
std::string SocialController::executeCurl(const std::string& method, const std::string& url, const std::string& data) {
    std::string cmd;
    if (method == "GET") {
        cmd = "curl -k -s \"" + url + "\"";
    }
    else if (method == "POST") {
        // If data is empty, send without -d to avoid errors
        if (data.empty())
            cmd = "curl -k -s -X POST \"" + url + "\"";
        else
            cmd = "curl -k -s -X POST \"" + url + "\" -d \"" + data + "\"";
    }
    else if (method == "DELETE") {
        cmd = "curl -k -s -X DELETE \"" + url + "\"";
    }
    else {
        // fallback to GET
        cmd = "curl -k -s \"" + url + "\"";
    }

    std::string output;
    std::array<char, 256> buffer;

    FILE* pipe = POPEN(cmd.c_str(), "r");
    if (pipe) {
        while (fgets(buffer.data(), static_cast<int>(buffer.size()), pipe) != nullptr) {
            output += buffer.data();
        }
        PCLOSE(pipe);
    }
    return output;
}

void SocialController::sendFriendRequest(const HttpRequestPtr& /*req*/, std::function<void(const HttpResponsePtr&)>&& callback,
    const std::string& fromName, const std::string& toName) {

    std::string url = firebaseBaseUrl + "invitatii/" + toName + ".json";
    // Firebase entries used earlier were simple JSON strings like: "SenderName"
    std::string data = "\\\"" + fromName + "\\\"";

    executeCurl("POST", url, data);

    Json::Value res;
    res["status"] = "Succes";
    res["mesaj"] = "Cerere trimisa de la " + fromName + " catre " + toName;
    callback(HttpResponse::newHttpJsonResponse(res));
}

// 2. VEZI CERERI PENDING (GET)
void SocialController::getPendingRequests(const HttpRequestPtr& req, std::function<void(const HttpResponsePtr&)>&& callback, std::string userName) {
    std::string url = firebaseBaseUrl + "invitatii/" + userName + ".json";
    std::string responseData = executeCurl("GET", url);

    Json::Value all, res(Json::arrayValue);
    Json::Reader reader;
    if (reader.parse(responseData, all) && !all.isNull()) {
        for (auto const& id : all.getMemberNames()) {
            res.append(all[id].asString());
        }
    }
    callback(HttpResponse::newHttpJsonResponse(res));
}

// 3. R?SPUNDE LA CERERE (POST)
void SocialController::respondToRequest(const HttpRequestPtr& req, std::function<void(const HttpResponsePtr&)>&& callback, std::string userName, std::string friendName, bool accept) {
    if (accept) {
        // Adaug? reciproc în nodul "prieteni"
        std::string dataUser = "\\\"" + friendName + "\\\"";
        std::string dataFriend = "\\\"" + userName + "\\\"";

        executeCurl("POST", firebaseBaseUrl + "prieteni/" + userName + ".json", dataUser);
        executeCurl("POST", firebaseBaseUrl + "prieteni/" + friendName + ".json", dataFriend);
    }

    // ?terge cererea din pending (cur???m tot nodul de invita?ii pentru simplitate)
    executeCurl("DELETE", firebaseBaseUrl + "invitatii/" + userName + ".json");

    Json::Value res;
    res["status"] = "success";
    res["mesaj"] = accept ? "Cerere acceptat?!" : "Cerere refuzat?.";
    callback(HttpResponse::newHttpJsonResponse(res));
}

// 4. LISTA DE PRIETENI (GET)
void SocialController::getFriendsList(const HttpRequestPtr& req, std::function<void(const HttpResponsePtr&)>&& callback, std::string userName) {
    std::string url = firebaseBaseUrl + "prieteni/" + userName + ".json";
    std::string responseData = executeCurl("GET", url);

    Json::Value all, res(Json::arrayValue);
    Json::Reader reader;
    if (reader.parse(responseData, all) && !all.isNull()) {
        for (auto const& id : all.getMemberNames()) {
            res.append(all[id].asString());
        }
    }
    callback(HttpResponse::newHttpJsonResponse(res));
}