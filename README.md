News App

The News App is a feature-rich application that allows users to stay updated with the latest news from various sources. It utilizes the Model-View-ViewModel (MVVM) architecture, Retrofit for API integration, Firebase for authentication and storage, and ChatGPT API for interactive chat functionality. The app also offers a weather forecast feature using a weather API and provides a dark theme option for enhanced user experience.
Features

    Browse and read news articles from popular sources.
    Categorized news sections for easy navigation.
    Search functionality to find news articles based on keywords.
    Bookmark favorite articles for quick access.
    Get real-time weather updates based on the user's location.
    ChatGPT integration for interactive and personalized news conversations.
    User authentication using Firebase Authentication and Google Sign-In.
    Reset forgotten passwords via the Forget Password feature.
    Option to switch between light and dark themes for personalized app appearance.
    Save user preferences and settings using Firebase Firestore.

Screenshots

Include a few screenshots showcasing the user interface and key features of your app. For example:



![Screenshot_2023-06-24-03-11-23-85_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/5d15e15f-1610-4494-9da1-4fc7ff4a6771)
![Screenshot_2023-06-24-03-11-23-85_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/b69d1f14-fcfb-4bdb-a2d7-cb9859cb91d0)
![Screenshot_2023-06-24-03-11-05-74_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/798e3624-6fea-4fe7-ab90-4a02b56163dc)
![Screenshot_2023-06-24-03-09-49-44_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/bed52e1a-6e10-4b28-b670-67305e1ef57a)
![Screenshot_2023-06-24-03-09-22-58_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/b8513b05-a468-4024-a80d-4761bfeb5c85)
![Screenshot_2023-06-24-03-08-17-95_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/a9c5e43b-c621-46ee-9f3c-aad843f64474)
![Screenshot_2023-06-13-14-19-25-68_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/be31762d-cb40-40f7-8214-9ec72e5da681)
![Screenshot_2023-06-13-14-19-20-89_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/27d6c6a4-3ff3-4620-a604-90fb3d14513d)
![Screenshot_2023-06-24-03-10-56-78_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/d327a672-8539-4f78-82a1-ef1a032248e4)
![Screenshot_2023-06-24-03-10-49-40_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/bea27220-775f-4b96-af5d-a066ea0a51b6)
![Screenshot_2023-06-24-03-10-30-70![Screenshot_2023-06-24-03-11-43-09_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/d80859e3-dced-4927-893c-98118651247a)
![Screenshot_2023-06-24-03-11-23-85_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/75c4f100-3ca0-4ba1-b89b-b3baaac7cc86)
![Screenshot_2023-06-24-03-11-15-18_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/ce7fa067-3ca3-4cbb-8a9e-7661701afb3c)
![Screenshot_2023-06-24-03-11-05-74_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/d6052564-4a0c-40fc-b9b4-d41a8ea589e0)
_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/9d7bed56-482c-4761-89de-7392986e090a)
![Screenshot_2023-06-24-03-11-43-09_1ed2117fd6ac924e621ba25bc591f5c1](https://github.com/jester-sys/DailyNews-App/assets/115554090/de2e86cd-af7f-42a9-9afa-dfd85316cd35)


Architecture

The app follows the MVVM (Model-View-ViewModel) architecture, which separates the concerns of data management, UI rendering, and user interactions.

    Model: Handles data operations and API requests. It includes classes such as NewsRepository, WeatherRepository, and ChatRepository responsible for fetching news articles, weather data, and handling chat interactions.
    View: Displays the user interface and handles user interactions. It includes activities, fragments, and layout files responsible for rendering news articles, weather information, and chat conversations.
    ViewModel: Acts as a bridge between the Model and View components. It provides data to the View, handles user actions, and updates the Model accordingly. The ViewModel also abstracts the business logic, allowing for better testability and separation of concerns.

Usage

    Launch the app and log in using your Firebase or Google account credentials.
    Upon successful login, you will be directed to the main screen, displaying the latest news articles.
    Browse through the articles by selecting the desired category or using the search feature.
    Click on an article to view its detailed information, including the title, author, and content.
    Bookmark articles by tapping the bookmark icon for quick access to your favorite content.
    Access the weather forecast feature by navigating to the weather section or using the provided widget.
    Engage in interactive news conversations by accessing the chat feature powered by ChatGPT API.
    Customize the app's appearance by enabling the dark theme in the settings menu.
    Enjoy staying updated with the latest news and personalized features of the app!

Contributing
Contributions to the News App project are welcome! If you have any ideas, bug fixes, or new features to propose, please submit an issue or create a pull request following the contribution guidelines provided in the repository.

Contact
Provide your contact information Kanhaiyayadav7221@gmail.com
