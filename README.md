# Google Contacts Birthday Reminder

A Java/Kotlin application that fetches contact information from Google Contacts, including birthdays, and provides reminders for upcoming birthdays.

## Table of Contents

- [Introduction](#introduction)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Introduction

This application utilizes the Google People API to retrieve contact information, including birthdays, from your Google Contacts. It then provides reminders for upcoming birthdays, helping you keep track of your contacts' special days.

## Prerequisites

Before you begin, make sure you have the following:

- Java Development Kit (JDK) 8 or higher installed.
- Google API credentials: Follow the [Google API Quickstart](https://developers.google.com/people/quickstart) to set up your API credentials and download the `credentials.json` file.
- Gradle: The project uses Gradle for building and managing dependencies.

  ## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/google-contacts-birthday-reminder.git

2. Navigate to the project directory:
     ```bash
    cd google-contacts-birthday-reminder
     
3. Place the credentials.json file you downloaded from Google API Quickstart into the src/main/resources directory.

## Usage
1. Build the project using Gradle:
     ```bash
    ./gradlew build

2. Run the application:
     ```bash
    ./gradlew run
     
3. The application will fetch contact information and birthdays from your Google Contacts and provide reminders for upcoming birthdays.


## Contributing
Contributions are welcome! If you find any issues or have improvements to suggest, feel free to open a GitHub issue or submit a pull request.

## License
This project is licensed under the MIT License.
