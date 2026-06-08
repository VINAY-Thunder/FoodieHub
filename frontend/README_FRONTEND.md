# FoodieHub Frontend

This is a React + TypeScript frontend for the FoodieHub backend.

## Prerequisites
- Node.js (v18 or higher)
- npm

## Installation & Setup

1. **Navigate to the frontend directory**:
   ```bash
   cd frontend
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Environment Configuration**:
   Create a `.env` file in the `frontend` folder (already created) with:
   ```env
   VITE_API_BASE_URL=http://localhost:8080
   ```
   *(Ensure this matches your backend server port)*

## Running the App

1. **Start the development server**:
   ```bash
   npm run dev
   ```

2. **Access the app**:
   Open your browser and go to: `http://localhost:5173`

## Project Structure
- `src/api`: All API calls to the backend.
- `src/pages/customer`: Customer-facing pages (Menu, History, Profile).
- `src/pages/admin`: Admin management pages (Dashboard, Menu, Orders, Inventory).
- `src/store`: Cart state management.
- `src/types`: TypeScript interfaces mirroring backend DTOs.

## Note
Authentication and login pages have been intentionally omitted as per requirements.
