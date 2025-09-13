import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'https://png-steganography-backend.herokuapp.com';

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
});

export interface SteganographyRecord {
  id: string;
  fileName: string;
  textHash: string;
  transactionHash: string;
  blockchainAddress: string;
  createdAt: string;
  status: string;
  originalFileName: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
  record?: SteganographyRecord;
  records?: SteganographyRecord[];
  extractedText?: string;
  verified?: boolean;
  hash?: string;
  timestamp?: number;
  count?: number;
}

export const steganographyAPI = {
  // Hide text in image
  hideText: async (imageFile: File, text: string): Promise<ApiResponse<SteganographyRecord>> => {
    const formData = new FormData();
    formData.append('image', imageFile);
    formData.append('text', text);
    
    const response = await api.post('/hide', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    return response.data;
  },

  // Extract and verify text from image
  extractText: async (imageFile: File, transactionHash: string): Promise<ApiResponse<string>> => {
    const formData = new FormData();
    formData.append('image', imageFile);
    formData.append('transactionHash', transactionHash);
    
    const response = await api.post('/extract', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    return response.data;
  },

  // Verify text integrity
  verifyText: async (text: string, transactionHash: string): Promise<ApiResponse<boolean>> => {
    const formData = new FormData();
    formData.append('text', text);
    formData.append('transactionHash', transactionHash);
    
    const response = await api.post('/verify', formData);
    return response.data;
  },

  // Get all records
  getAllRecords: async (): Promise<ApiResponse<SteganographyRecord[]>> => {
    const response = await api.get('/records');
    return response.data;
  },

  // Get record by ID
  getRecordById: async (id: string): Promise<ApiResponse<SteganographyRecord>> => {
    const response = await api.get(`/records/${id}`);
    return response.data;
  },

  // Get record by transaction hash
  getRecordByTransactionHash: async (transactionHash: string): Promise<ApiResponse<SteganographyRecord>> => {
    const response = await api.get(`/records/transaction/${transactionHash}`);
    return response.data;
  },

  // Generate hash
  generateHash: async (text: string): Promise<ApiResponse<string>> => {
    const formData = new FormData();
    formData.append('text', text);
    
    const response = await api.post('/hash', formData);
    return response.data;
  },

  // Health check
  healthCheck: async (): Promise<ApiResponse<any>> => {
    const response = await api.get('/health');
    return response.data;
  },
};

export default steganographyAPI;
