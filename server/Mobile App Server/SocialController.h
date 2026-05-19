#pragma once
#include <drogon/HttpController.h>
#include <string>

using namespace drogon;

class SocialController : public drogon::HttpController<SocialController> {
public:
    METHOD_LIST_BEGIN
        ADD_METHOD_TO(SocialController::sendFriendRequest, "/send_friend_request?fromName={fromName}&toName={toName}", Post);
        ADD_METHOD_TO(SocialController::getPendingRequests, "/get_pending_requests?userName={userName}", Get);
        ADD_METHOD_TO(SocialController::respondToRequest, "/respond_to_request?userName={userName}&friendName={friendName}&accept={accept}", Post);
        ADD_METHOD_TO(SocialController::getFriendsList, "/get_friends?userName={userName}", Get);
    METHOD_LIST_END

    void sendFriendRequest(const HttpRequestPtr& req, std::function<void(const HttpResponsePtr&)>&& callback,
        const std::string& fromName, const std::string& toName);
    void getPendingRequests(const HttpRequestPtr& req, std::function<void(const HttpResponsePtr&)>&& callback,
        const std::string& userName);
    void respondToRequest(const HttpRequestPtr& req, std::function<void(const HttpResponsePtr&)>&& callback,
        const std::string& userName, const std::string& friendName, const std::string& accept);
    void getFriendsList(const HttpRequestPtr& req, std::function<void(const HttpResponsePtr&)>&& callback,
        const std::string& userName);

private:
    const std::string firebaseBaseUrl = "https://mymobileapp-78269-default-rtdb.europe-west1.firebasedatabase.app/";
    std::string executeCurl(const std::string& method, const std::string& url, const std::string& data = "");
};