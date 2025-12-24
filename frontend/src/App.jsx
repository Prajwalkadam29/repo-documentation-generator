import React, { useState } from "react";
import {
  Github,
  FolderCode,
  Sparkles,
  Copy,
  Check,
  Loader2,
  AlertCircle,
  Terminal,
} from "lucide-react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import api from "./api/axios";

function App() {
  const [activeTab, setActiveTab] = useState("github");
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState("");
  const [error, setError] = useState("");
  const [copied, setCopied] = useState(false);

  const [formData, setFormData] = useState({
    githubUrl: "",
    localPath: "",
    aiService: "gemini",
  });

  const handleGenerate = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setResult("");

    try {
      const response = await api.post("/generate", formData);
      if (response.data.success) {
        setResult(response.data.data);
      } else {
        setError(response.data.message);
      }
    } catch (err) {
      const msg =
        err.response?.data?.message ||
        "Connection to backend failed. Check if Spring Boot is running.";
      setError(msg);
    } finally {
      setLoading(false);
    }
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(result);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  const downloadReadme = () => {
    const element = document.createElement("a");
    const file = new Blob([result], { type: "text/markdown" });
    element.href = URL.createObjectURL(file);
    element.download = "README.md";
    document.body.appendChild(element); // Required for this to work in FireFox
    element.click();
  };

  return (
    <div className="min-h-screen bg-slate-50 py-12 px-4 sm:px-6 lg:px-8 font-sans">
      <div className="max-w-5xl mx-auto">
        {/* Header Section */}
        <div className="text-center mb-10">
          <div className="inline-flex items-center justify-center p-2 bg-blue-100 rounded-2xl mb-4">
            <Sparkles className="text-blue-600" size={28} />
          </div>
          <h1 className="text-4xl font-extrabold text-slate-900 tracking-tight">
            README Generator <span className="text-blue-600">Pro</span>
          </h1>
          <p className="mt-3 text-lg text-slate-600 max-w-2xl mx-auto">
            Turn your codebase into high-quality documentation instantly using
            Gemini, Groq, or Local LLMs.
          </p>
        </div>

        <div className="bg-white rounded-3xl shadow-2xl shadow-slate-200/60 border border-slate-200 overflow-hidden">
          {/* Tabs */}
          <div className="flex bg-slate-50/50 p-1">
            <button
              onClick={() => setActiveTab("github")}
              className={`flex-1 flex items-center justify-center gap-2 py-3 px-4 rounded-2xl text-sm font-semibold transition-all ${
                activeTab === "github"
                  ? "bg-white text-blue-600 shadow-sm ring-1 ring-slate-200"
                  : "text-slate-500 hover:text-slate-700"
              }`}
            >
              <Github size={18} /> GitHub Repository
            </button>
            <button
              onClick={() => setActiveTab("local")}
              className={`flex-1 flex items-center justify-center gap-2 py-3 px-4 rounded-2xl text-sm font-semibold transition-all ${
                activeTab === "local"
                  ? "bg-white text-blue-600 shadow-sm ring-1 ring-slate-200"
                  : "text-slate-500 hover:text-slate-700"
              }`}
            >
              <FolderCode size={18} /> Local Project
            </button>
          </div>

          {/* Form */}
          <form onSubmit={handleGenerate} className="p-8">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 items-end">
              <div className="md:col-span-2">
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-2 ml-1">
                  {activeTab === "github"
                    ? "Repository URL"
                    : "Local Directory Path"}
                </label>
                <div className="relative">
                  <input
                    type="text"
                    required
                    placeholder={
                      activeTab === "github"
                        ? "https://github.com/user/repo"
                        : "C:/Users/name/project"
                    }
                    className="w-full bg-slate-50 border-0 ring-1 ring-slate-200 rounded-xl px-4 py-3 focus:ring-2 focus:ring-blue-500 outline-none transition-all placeholder:text-slate-400"
                    value={
                      activeTab === "github"
                        ? formData.githubUrl
                        : formData.localPath
                    }
                    onChange={(e) =>
                      setFormData({
                        ...formData,
                        [activeTab === "github" ? "githubUrl" : "localPath"]:
                          e.target.value,
                      })
                    }
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold uppercase tracking-wider text-slate-500 mb-2 ml-1">
                  AI Engine
                </label>
                <select
                  className="w-full bg-slate-50 border-0 ring-1 ring-slate-200 rounded-xl px-4 py-3 focus:ring-2 focus:ring-blue-500 outline-none appearance-none cursor-pointer"
                  value={formData.aiService}
                  onChange={(e) =>
                    setFormData({ ...formData, aiService: e.target.value })
                  }
                >
                  <option value="gemini">Google Gemini 1.5</option>
                  <option value="groq">Groq (Llama 3.3)</option>
                  <option value="ollama">Ollama (Local)</option>
                </select>
              </div>
            </div>

            <button
              disabled={loading}
              className="mt-8 w-full bg-slate-900 hover:bg-black text-white font-bold py-4 rounded-xl transition-all flex items-center justify-center gap-3 disabled:bg-slate-300 disabled:cursor-not-allowed group"
            >
              {loading ? (
                <Loader2 className="animate-spin" size={20} />
              ) : (
                <Sparkles
                  className="group-hover:rotate-12 transition-transform"
                  size={20}
                />
              )}
              {loading ? "Analyzing Source Code..." : "Generate README.md"}
            </button>
          </form>

          {/* Error Message */}
          {error && (
            <div className="mx-8 mb-8 p-4 bg-red-50 border border-red-100 rounded-xl flex items-start gap-3 text-red-700">
              <AlertCircle size={20} className="mt-0.5 flex-shrink-0" />
              <p className="text-sm font-medium">{error}</p>
            </div>
          )}

          {/* Markdown Preview Area */}
          {result && (
            <div className="border-t border-slate-100 bg-white">
              <div className="flex items-center justify-between px-8 py-4 bg-slate-50 border-b border-slate-100">
                <div className="flex items-center gap-2 text-slate-600">
                  <Terminal size={18} />
                  <span className="text-xs font-bold uppercase tracking-widest">
                    Live Preview
                  </span>
                </div>
                <button
                  onClick={copyToClipboard}
                  className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-white border border-slate-200 text-sm font-semibold text-slate-700 hover:bg-slate-50 transition-all shadow-sm"
                >
                  {copied ? (
                    <Check size={16} className="text-green-600" />
                  ) : (
                    <Copy size={16} />
                  )}
                  {copied ? "Copied" : "Copy"}
                </button>
                <button
                  onClick={downloadReadme}
                  className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-blue-600 text-white text-sm font-semibold hover:bg-blue-700 transition-all shadow-sm"
                >
                  <FolderCode size={16} /> Download .md
                </button>
              </div>

              <div className="p-8 prose prose-slate prose-lg max-w-none prose-pre:bg-slate-900 prose-pre:text-slate-100 prose-code:text-blue-600">
                <ReactMarkdown remarkPlugins={[remarkGfm]}>
                  {result}
                </ReactMarkdown>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
