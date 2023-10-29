Sender Software:
    1. Prompts the user to input server IP addresses and corresponding database names. (since they may vary based on peoples implementation)

    2. When users are done entering database IPs and names, they can exit out of the loop by typing "exit"

    3. For each provided server IP and database name, create and connect a thread to it.

    4. Enters an infinite loop where:
        It prompts the user to enter a message.
        Selects a random thread.
        Starts the thread, which inserts the entered message into the specified database's ASYNC_MESSAGE table.