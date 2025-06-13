import {useEffect, useState} from "react";
import {AssistantService, CheesePlatterService} from "Frontend/generated/endpoints";
import CheeseDetails from '../generated/com/example/cheesePlatterAgent/data/CheeseDetails';
import {GridColumn} from "@vaadin/react-components/GridColumn";
import {Grid} from "@vaadin/react-components/Grid";
import {MessageInput} from "@vaadin/react-components/MessageInput";
import {nanoid} from "nanoid";
import {SplitLayout} from "@vaadin/react-components/SplitLayout";
import {MessageItem} from "../components/Message";
import MessageList from "../components/MessageList";

export default function Index() {
    const [chatId, setChatId] = useState(nanoid());
    const [working, setWorking] = useState(false);
    const [userId, setUserId] = useState<number | undefined>();
    const [customerName, setCustomerName] = useState<string | undefined>('');
    const [cheeses, setCheeses] = useState<CheeseDetails[]>([]);
    const [messages, setMessages] = useState<MessageItem[]>([{
        role: 'assistant',
        content: 'Welcome to Cheesy Stuff! Would you like some help to compose the cheese platter of your dreams? Please first provide your username.'
    }]);

    useEffect(() => {
        // Update bookings when we have received the full response
        if (!working) {
            CheesePlatterService.getCurrentUserId().then(setUserId);
            CheesePlatterService.getFullNameOfUser(userId).then(setCustomerName);
            // @ts-ignore
            CheesePlatterService.getCheesePlatter(userId).then(setCheeses);
        }
    }, [working]);

    function addMessage(message: MessageItem) {
        setMessages(messages => [...messages, message]);
    }

    function appendToLatestMessage(chunk: string) {
        setMessages(messages => {
            const latestMessage = messages[messages.length - 1];
            latestMessage.content += chunk;
            return [...messages.slice(0, -1), latestMessage];
        });
    }

    async function sendMessage(message: string) {
        setWorking(true);
        addMessage({
            role: 'user',
            content: message
        });

        let first = true;
        AssistantService.chat(chatId, message)
            .onNext(token => {
                let tokenS = token as string;
                if (first && tokenS) {
                    addMessage({
                        role: 'assistant',
                        content: tokenS
                    });
                    first = false;
                } else {
                    appendToLatestMessage(tokenS);
                }
            })
            .onError(() => setWorking(false))
            .onComplete(() => setWorking(false));
    }

    return (
        <SplitLayout className="h-full">
            <div className="flex flex-col gap-m p-m box-border h-full" style={{width: '30%'}}>
                <h3>Cheesy support</h3>
                <MessageList messages={messages} className="flex-grow overflow-scroll"/>
                <MessageInput onSubmit={e => sendMessage(e.detail.value)} className="px-0"/>
            </div>
            <div className="flex flex-col gap-m p-m box-border" style={{width: '70%'}}>
                <h3>Cheese platter of {customerName}</h3>
                <Grid items={cheeses} className="flex-shrink-0">
                    <GridColumn path="name" autoWidth/>
                    <GridColumn path="typeDescription" header="type" autoWidth/>
                    <GridColumn path="producer" autoWidth/>
                    <GridColumn path="description" autoWidth/>
                    <GridColumn path="price" header="price per kg" autoWidth/>
                </Grid>
            </div>
        </SplitLayout>
    );
}
