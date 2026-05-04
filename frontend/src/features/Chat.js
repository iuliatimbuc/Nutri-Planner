import React from "react";
import Container from "@mui/material/Container";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Avatar from "@mui/material/Avatar";
import axiosInstance from "../helper/axios";
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

class Chat extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            messages: [],
            newMessage: "",
            receiverId: null,
            receiverName: "",
            searchQuery: "",
            searchResults: [],
            recentConversations: [],
            currentUserId: localStorage.getItem("USER_ID")
        };
        this.client = null;
        this.messagesEndRef = React.createRef(); // ref scroll
    }

    componentDidMount() {
        const userId = localStorage.getItem("USER_ID");

        this.client = new Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
            onConnect: () => {
                this.client.subscribe(`/topic/chat/${userId}`, (message) => {
                    const received = JSON.parse(message.body); // obiectul MassageDTO
                    const { receiverId } = this.state;

                    // afiseaza DOAR daca mesajul e din conversatia activa
                    if (String(received.senderId) === String(receiverId) ||
                        String(received.receiverId) === String(receiverId)) {
                        this.setState(prev => ({
                            messages: [...prev.messages, received]
                        }), this.scrollToBottom);
                    }
                });
            }
        });
        this.client.activate();
        this.loadRecentConversations();
    }

    componentWillUnmount() {
        if (this.client) this.client.deactivate();
    }

    scrollToBottom = () => {
        this.messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
    };

    loadRecentConversations = () => {
        const userId = localStorage.getItem("USER_ID");
        axiosInstance.get(`/messages/recent?userId=${userId}`)
            .then(res => this.setState({ recentConversations: res.data }))
            .catch(err => console.log(err));
    };

    loadConversation = () => {
        const { currentUserId, receiverId } = this.state;
        if (!receiverId) return;
        axiosInstance.get(`/messages/conversation?userId1=${currentUserId}&userId2=${receiverId}`)
            .then(res => this.setState({ messages: res.data }, this.scrollToBottom))
            .catch(err => console.log(err));
    };

    selectUser = (user) => {
        this.setState({
            receiverId: user.id,
            receiverName: user.name,
            searchResults: [],
            searchQuery: ""
        }, this.loadConversation);
    };

    searchUsers = (query) => {
        if (!query || query.length < 2) {
            this.setState({ searchResults: [] });
            return;
        }
        axiosInstance.get(`/messages/search?name=${query}`)
            .then(res => this.setState({ searchResults: res.data }))
            .catch(err => console.log(err));
    };

    sendMessage = () => {
        const { currentUserId, receiverId, newMessage } = this.state;
        if (!newMessage.trim() || !receiverId) return;

        axiosInstance.post(`/messages/send?senderId=${currentUserId}&receiverId=${receiverId}&content=${newMessage}`)
            .then(res => {
                this.setState(prev => ({
                    messages: [...prev.messages, res.data],
                    newMessage: ""
                }), this.scrollToBottom);
            })
            .catch(err => console.log(err));
    };

    render() {
        const { messages, newMessage, receiverId, receiverName,
            searchQuery, searchResults, recentConversations, currentUserId } = this.state;

        return (
            <Container maxWidth="sm" sx={{ pb: 4 }}>

                {/* Header */}
                <Card sx={{ mt: 3, mb: 2, borderRadius: 4, boxShadow: "none", border: "0.5px solid rgba(0,0,0,0.1)" }}>
                    <CardContent sx={{ p: 2 }}>
                        <Typography variant="h5" fontWeight={500}>Messages</Typography>
                    </CardContent>
                </Card>

                {/* Search */}
                <Card sx={{ mb: 2, borderRadius: 4, boxShadow: "none", border: "0.5px solid rgba(0,0,0,0.1)" }}>
                    <CardContent sx={{ p: 2 }}>
                        <Box sx={{ display: "flex", gap: 1, mb: 1 }}>
                            <TextField
                                fullWidth
                                size="small"
                                label="Search user by name..."
                                value={searchQuery}
                                onChange={e => {
                                    this.setState({ searchQuery: e.target.value });
                                    this.searchUsers(e.target.value);
                                }}
                                />
                        </Box>

                        {/* Search results */}
                        {searchResults.map(user => (
                            <Box key={user.id} onClick={() => this.selectUser(user)}
                                 sx={{
                                     p: 1.5, borderRadius: 2, cursor: "pointer",
                                     "&:hover": { backgroundColor: "#F5F5F5" },
                                     display: "flex", justifyContent: "space-between",
                                     alignItems: "center"
                                 }}>
                                <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                                    <Avatar sx={{ width: 32, height: 32, backgroundColor: "#1D9E75", fontSize: 14 }}>
                                        {user.name?.charAt(0).toUpperCase()}
                                    </Avatar>
                                    <Typography fontWeight={500}>{user.name}</Typography>
                                </Box>
                                <Typography variant="caption" color="text.secondary">{user.email}</Typography>
                            </Box>
                        ))}
                    </CardContent>
                </Card>

                {/* Recent conversations */}
                {!receiverId && recentConversations.length > 0 && (
                    <Card sx={{ mb: 2, borderRadius: 4, boxShadow: "none", border: "0.5px solid rgba(0,0,0,0.1)" }}>
                        <CardContent sx={{ p: 2 }}>
                            <Typography variant="caption" color="text.secondary"
                                        sx={{ textTransform: "uppercase", letterSpacing: "0.08em", fontWeight: 500, display: "block", mb: 1 }}>
                                Recent Conversations
                            </Typography>
                            {recentConversations.map((conv, index) => (
                                <Box key={index}
                                     onClick={() => this.selectUser({ id: conv.userId, name: conv.userName })}
                                     sx={{
                                         p: 1.5, borderRadius: 2, cursor: "pointer",
                                         "&:hover": { backgroundColor: "#F5F5F5" },
                                         display: "flex", alignItems: "center", gap: 1.5
                                     }}>
                                    <Avatar sx={{ width: 40, height: 40, backgroundColor:"#1D9E75", fontSize: 16 }}>
                                        {conv.userName?.charAt(0).toUpperCase()}
                                    </Avatar>
                                    <Box flex={1}>
                                        <Typography fontWeight={500} fontSize={14}>{conv.userName}</Typography>
                                        <Typography variant="caption" color="text.secondary" noWrap>
                                            {conv.lastMessage}
                                        </Typography>
                                    </Box>
                                    <Typography variant="caption" color="text.secondary" sx={{ ml: "auto" }}>
                                        {conv.timestamp ? new Date(conv.timestamp).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}) : ""}
                                    </Typography>
                                </Box>
                            ))}
                        </CardContent>
                    </Card>
                )}

                {/* Chat window */}
                {receiverId && (
                    <>
                        {/* Chat header */}
                        <Card sx={{ mb: 1, borderRadius: 4, boxShadow: "none", border: "0.5px solid rgba(0,0,0,0.1)" }}>
                            <CardContent sx={{ p: 2, display: "flex", alignItems: "center", gap: 1 }}>
                                <Avatar sx={{ width: 36, height: 36, backgroundColor: "#1D9E75" }}>
                                    {receiverName?.charAt(0).toUpperCase()}
                                </Avatar>
                                <Typography fontWeight={500}>{receiverName}</Typography>
                                <Button size="small" onClick={() => this.setState({ receiverId: null, receiverName: "", messages: [] })}
                                        sx={{ ml: "auto", textTransform: "none", color: "text.secondary" }}>
                                     Close
                                </Button>
                            </CardContent>
                        </Card>

                        {/* Mesaje */}
                        <Card sx={{ mb: 1, borderRadius: 4, boxShadow: "none", border: "0.5px solid rgba(0,0,0,0.1)" }}>
                            <CardContent sx={{ p: 2 }}>
                                <Box sx={{ height: 350, overflowY: "auto", display: "flex", flexDirection: "column", gap: 1 }}>
                                    {messages.length === 0 ? (
                                        <Typography variant="body2" color="text.secondary" textAlign="center" mt={4}>
                                            No messages yet. Say hello!
                                        </Typography>
                                    ) : (
                                        messages.map((msg, index) => {
                                            const isMine = String(msg.senderId) === String(currentUserId);
                                            return (
                                                <Box key={index} sx={{ display: "flex", justifyContent: isMine ? "flex-end" : "flex-start" }}>
                                                    <Box sx={{
                                                        maxWidth: "70%",
                                                        backgroundColor: isMine ? "#1D9E75" : "#F0F0F0",
                                                        color: isMine ? "white" : "black",
                                                        borderRadius: isMine ? "16px 16px 4px 16px" : "16px 16px 16px 4px",
                                                        px: 2, py: 1
                                                    }}>
                                                        <Typography variant="body2">{msg.content}</Typography>
                                                        <Typography variant="caption" sx={{ opacity: 0.7, fontSize: 10, display: "block", textAlign: "right" }}>
                                                            {msg.timestamp ? new Date(msg.timestamp).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) : ""}
                                                        </Typography>
                                                    </Box>
                                                </Box>
                                            );
                                        })
                                    )}
                                    <div ref={this.messagesEndRef} />
                                </Box>
                            </CardContent>
                        </Card>

                        {/* Input mesaj */}
                        <Card sx={{ borderRadius: 4, boxShadow: "none", border: "0.5px solid rgba(0,0,0,0.1)" }}>
                            <CardContent sx={{ p: 2 }}>
                                <Box sx={{ display: "flex", gap: 1 }}>
                                    <TextField
                                        fullWidth size="small"
                                        placeholder="Type a message..."
                                        value={newMessage}
                                        onChange={e => this.setState({ newMessage: e.target.value })}
                                    />
                                    <Button variant="contained" onClick={this.sendMessage}
                                            sx={{ borderRadius: 2, textTransform: "none", backgroundColor: "#1D9E75" }}>
                                        Send
                                    </Button>
                                </Box>
                            </CardContent>
                        </Card>
                    </>
                )}
            </Container>
        );
    }
}

export default Chat;