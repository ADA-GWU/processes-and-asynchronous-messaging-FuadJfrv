Sender Software:

    1. Enters a loop, where it asks the user to input database IP address and corresponding database name. (since they may vary based on peoples implementation)

    2. When users are done entering database IPs and names, they can exit out of the loop by typing "exit"

    3. For each provided server IP and database name, create and connect a thread to it.

    4. Enters an infinite loop where:
        It prompts the user to enter a message.
        Selects a random thread.
        Starts the thread, which inserts the entered message into the specified database's ASYNC_MESSAGE table.


Reader Software:

    1. Exactly in the same way as Sender Software, enters a loop and asks the user to input database IP address and corresponding database name.

    2. When users are done entering database IPs and names, they can exit out of the loop by typing "exit"

    3. For each provided server IP and database name, create and connect a thread to it.

    4. Each thread constantly checks for available messages from their connected database

    5. If an available message is found, the thread prints it, and then marks it as received by updating its received time. (it ensures it picks the correct message by also checking its ID) (also locks the message with FOR UPDATE)

    6. Waits 3 seconds to allow others to read as well.

    7. loop back to step 4.