# 📝 Resume Checker  

## 📌 Overview  
RAG Resume Checker is an AI-powered application that analyzes resumes and detects **common mistakes** to help job seekers improve their applications.

## 🚀 Features  
- **Common Mistake Detection**: Identifies issues like missing skills, poor formatting, vague descriptions, or keyword mismatches.  
- **AI-Powered Resume Analysis**: Uses **Spring AI** with **Mistral AI** for intelligent feedback.  
- **Vector Search for Matching**: Implements **Pgvector** for semantic job-resume comparison.  
- **Personalized Suggestions**: Helps refine resumes based on industry best practices.  
- **Fast & Scalable**: Built with **Java + Spring Boot**, ensuring reliability and performance.  

## 🛠️ Tech Stack  
- **Java 17+**  
- **Spring Boot + Spring AI**  
- **Mistral AI** (for LLM-based resume analysis)  
- **PostgreSQL + Pgvector** (for semantic search)  
- **Docker** (for containerized deployment)  

## 🏃 Usage
Send a request to analyze a resume
```
POST /api/resume/analyze  
Content-Type: application/json  

{
  "resumeText": "Your resume content here"
}

```
Receive AI-generated feedback:
```
{
  "matchScore": 78,
  "commonMistakes": [
    "Lack of action verbs in work experience",
    "No mention of relevant certifications",
  ],
  "improvements": [
    "Use strong action verbs to describe experience",
    "Add industry-recognized certifications",
    "Be more specific about achievements in past roles"
  ]
}
```
